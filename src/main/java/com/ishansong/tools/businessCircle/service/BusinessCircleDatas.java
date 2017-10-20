package com.ishansong.tools.businessCircle.service;

import com.ishansong.tools.businessCircle.model.LatLng;
import com.ishansong.tools.businessCircle.utils.PathUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商圈坐标数据读取
 * @author wubin
 * @date 2017/10/2
 **/
public class BusinessCircleDatas {

    private static final Logger logger = LoggerFactory.getLogger(BusinessCircleDatas.class);

    private static String encoding = "UTF-8";

    private List<File> getBuseinssCircleFiles() {

        List<File> returnFiles = new ArrayList<>();
        try {
            File dir = new File(PathUtil.getBusinessCirclesFilePath());
            File[] files = dir.listFiles();
            logger.info("商圈文件：");
            for (File file : files) {

                if(file.getName().endsWith(".txt")
                        && file.getName().contains("商圈")) {
                    returnFiles.add(file);
                    logger.info(file.getPath());
                }
            }
            return returnFiles;
        } catch (Exception e) {
            logger.error("获取商圈数据文件出现异常:{}", ExceptionUtils.getStackTrace(e));
        }
        return returnFiles;
    }

    public Map<String, List<LatLng>> getBusinessCircleDatas() {

        List<File> files = getBuseinssCircleFiles();
        Map<String, List<LatLng>> resultMap = new HashMap<>();
        for (File file : files) {

            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                logger.error(file.getName() + ":文件丢失,继续解析下个商圈数据。");
                continue;
            }

            InputStreamReader read = null;//考虑到编码格式
            try {
                read = new InputStreamReader(fis, encoding);
            } catch (UnsupportedEncodingException e) {
                logger.error(file.getName() + ":存在不支持的编码格式,继续解析下个商圈数据。");
                e.printStackTrace();
                continue;
            }

            // 文件名=商圈名称
            String fileName = PathUtil.cutFileSuffix(file.getName());
            resultMap.put(fileName, new ArrayList<LatLng>());
            logger.info(fileName+"坐标数据：");

            // 组装商圈坐标数据
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            try {
                while (StringUtils.isNotBlank(lineTxt = bufferedReader.readLine())) {

                    logger.info(lineTxt);
                    String[] xy = lineTxt.split(",");

                    if(xy.length <= 1
                            || !NumberUtils.isNumber(xy[0])
                            || !NumberUtils.isNumber(xy[1])) continue;

                    double lng = NumberUtils.toDouble(xy[0]);
                    double lat = NumberUtils.toDouble(xy[1]);
                    LatLng point = new LatLng(lat, lng);

                    List<LatLng> points = resultMap.get(fileName);
                    points.add(point);
                    resultMap.put(fileName, points);
                }

                read.close();
            } catch (IOException e) {
                logger.error("读取商圈数据文件出现异常："+ExceptionUtils.getStackTrace(e));
            }
        }
        if(resultMap.isEmpty())
            throw new RuntimeException("商圈数据为空，请检查！");
        return resultMap;
    }
}
