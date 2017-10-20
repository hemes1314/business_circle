package com.ishansong.tools.businessCircle.service;

import com.ishansong.tools.businessCircle.model.LatLng;
import com.ishansong.tools.businessCircle.utils.PathUtil;
import com.ishansong.tools.businessCircle.utils.SpatialRelationUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * 商户所属商圈Excel文件写入
 * @author wubin
 * @date 2017/10/9
 **/
public class CellerXlsxOverwrite {

    private static final Logger logger = LoggerFactory.getLogger(CellerXlsxOverwrite.class);

    private static int latIndex, lngIndex;
    private static String xlsxFilePath = null;

    public CellerXlsxOverwrite(String xlsxFilePath, String latIndex, String lngIndex) throws UnsupportedEncodingException {

        xlsxFilePath = xlsxFilePath.trim();
        if(!xlsxFilePath.contains("/")) {
            xlsxFilePath = PathUtil.getJarPath()+xlsxFilePath;
        }
        CellerXlsxOverwrite.xlsxFilePath = xlsxFilePath;
        CellerXlsxOverwrite.latIndex = NumberUtils.toInt(latIndex) - 1;
        CellerXlsxOverwrite.lngIndex = NumberUtils.toInt(lngIndex) - 1;
    }

    /**
     * 入口
     * @author wubin
     * @date 2017/10/11
     **/
    public void overwrite(Map<String, List<LatLng>> businessCircleDatas) throws Exception {

        // 获取文件对象
        Workbook workbook = getWorkBook();
        // 读取商家经纬度
        List<LatLng> cellerPoints = readCellerPointData(workbook);
        // 获取商家经纬度与商圈对应关系
        Map<String, String> celler2BusinessCircle = getLatLng2BusinessCircle(cellerPoints, businessCircleDatas);
        // 写入商圈
        writeBusinessCircle(workbook, cellerPoints, celler2BusinessCircle);
    }

    /**
     * 获取商家经纬度与商圈对应关系
     * @author wubin
     * @date 2017/10/11
     **/
    private Map<String, String> getLatLng2BusinessCircle(List<LatLng> cellerPoints, Map<String, List<LatLng>> businessCircleDatas) {

        Map<String, String> rtnMap = new HashMap<>();
        Iterator<String> it = businessCircleDatas.keySet().iterator();
        while(it.hasNext()) {

            String businessCircleName = it.next();

            for(LatLng cellerPoint : cellerPoints) {

                if(cellerPoint == null) continue;
                List<LatLng> bcPoints = businessCircleDatas.get(businessCircleName);
                boolean isContains = SpatialRelationUtil.isPolygonContainsPoint(bcPoints, cellerPoint);
                if(isContains) {
                    rtnMap.put(cellerPoint.toString(), businessCircleName);
                }
            }
        }
        return rtnMap;
    }

    /**
     * 写入商圈数据
     * @author wubin
     * @date 2017/10/11
     **/
    private void writeBusinessCircle(Workbook workbook, List<LatLng> cellerPoints, Map<String, String> celler2BusinessCircle) throws Exception {
        Sheet sheet = workbook.getSheetAt(0);

        logger.info("写入商圈数据...");

        // 生成"所属商圈"标题列
        Row row0 = sheet.getRow(0);
        Cell bcTitleCell = row0.createCell(row0.getLastCellNum(), Cell.CELL_TYPE_STRING);
        bcTitleCell.setCellStyle(row0.getCell(0).getCellStyle());
        bcTitleCell.setCellValue("所属商圈");

        // 添加商圈列
        int excelRowNum = sheet.getLastRowNum();
        for(int rowNum = 1; rowNum <= excelRowNum; rowNum++) {

            LatLng cellerPoint = cellerPoints.get(rowNum-1);
            if(cellerPoint == null) {
               continue;
            }
            Row row = sheet.getRow(rowNum);
            Cell bcValCell = row.createCell(row.getLastCellNum(), Cell.CELL_TYPE_STRING);
            bcValCell.setCellStyle(row.getCell(0).getCellStyle());

            // 获取商圈
            bcValCell.setCellValue(celler2BusinessCircle.get(cellerPoint.toString()));
        }

        // 生成文件
        xlsxFilePath = PathUtil.cutFileSuffix(xlsxFilePath)+"_output.xlsx";
        File outputFile = new File(xlsxFilePath);
        if(outputFile.exists()) outputFile.delete();

        FileOutputStream fos = new FileOutputStream(outputFile);
        OutputStream bos = new BufferedOutputStream(fos);
        workbook.write(bos);
        workbook.close();
        bos.flush();
        // 设置写入文件过程为同步调用
        // fos.getFD().sync();
        bos.close();
        logger.info("写入完成");
    }

    /**
     * 读取商户经纬度
     * @author wubin
     * @date 2017/10/9
     **/
    private List<LatLng> readCellerPointData(Workbook workbook) throws Exception {

        Sheet sheet = workbook.getSheetAt(0);

        List<LatLng> cellerPoints = new ArrayList<>();
        // 读取经纬度
        logger.info(xlsxFilePath+"经纬度");
        int totalValid = 0;
        int excelRowNum = sheet.getLastRowNum();
        for(int rowNum = 1; rowNum <= excelRowNum; rowNum++) {
            Row row = sheet.getRow(rowNum);
            Object latObj = row.getCell(latIndex);
            Object lngObj = row.getCell(lngIndex);
            String latStr = String.valueOf(latObj).trim();
            String lngStr = String.valueOf(lngObj).trim();
            logger.info(latStr+","+lngStr);

            if(NumberUtils.isNumber(latStr)
                    && NumberUtils.isNumber(lngStr)) {
                LatLng point = new LatLng();
                point.setLat(NumberUtils.toDouble(latStr));
                point.setLng(NumberUtils.toDouble(lngStr));
                cellerPoints.add(point);
                totalValid++;
            } else {
                cellerPoints.add(null);
            }
        }
        if(totalValid == 0) {
            throw new RuntimeException("\"" + xlsxFilePath + "\"无有效经纬度数据，请检查文件或输入参数！");
        }
        return cellerPoints;
    }

    /**
     * 获取Excel对象
     * @author wubin
     * @date 2017/10/11
     **/
    private Workbook getWorkBook() throws Exception {
        logger.info("\""+xlsxFilePath+"\"文件是否存在："+new File(xlsxFilePath).exists());

        //将excel初步解析为workbook;这一步需要蛮长时间
        Workbook workbook = WorkbookFactory.create(new FileInputStream(xlsxFilePath));
        //开始读EXCEL
        //获取excel的sheetSize
        int sheetSize = workbook.getNumberOfSheets();
        if(sheetSize < 1) {
            throw new RuntimeException("Excel文件无内容，请检查！");
        }
        return workbook;
    }

}
