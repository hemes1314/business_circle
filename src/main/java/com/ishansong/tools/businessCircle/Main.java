package com.ishansong.tools.businessCircle;

import com.ishansong.tools.businessCircle.model.LatLng;
import com.ishansong.tools.businessCircle.service.BusinessCircleDatas;
import com.ishansong.tools.businessCircle.service.CellerXlsxOverwrite;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Main
 * @author wubin
 * @date 2017/10/2
 **/
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    /**
     * 主入口
     * @Param args [fileName, latIndex, lngIndex]
     * @author wubin
     * @date 2017/10/11
     **/
    public static void main(String[] args) {

        logger.info("解析商圈数据start");

        try {
            // 模拟输入参数
            String filePath = "/Users/wubin/Downloads/成都商家信息POI.xlsx";
            String lngIndex = "14";
            String latIndex = "15";
//            if(args == null || args.length < 3) {
//                throw new RuntimeException("参数格式有误，参数格式：[商户文件名,纬度所在列数,经度所在列数");
//            }
//            String filePath = args[0];
//            String latIndex = args[1];
//            String lngIndex = args[2];
            validateParams(filePath, latIndex, lngIndex);

            // 读取商圈坐标数据
            BusinessCircleDatas businessCircleDatas = new BusinessCircleDatas();
            Map<String, List<LatLng>> datas = businessCircleDatas.getBusinessCircleDatas();

            // 重写Excel文件
            CellerXlsxOverwrite overwrite = new CellerXlsxOverwrite(filePath, latIndex, lngIndex);
            overwrite.overwrite(datas);
        } catch (Exception e) {
            logger.error("解析商圈数据异常:{}", ExceptionUtils.getStackTrace(e));
        }

        logger.info("解析商圈数据end");
    }

    /**
     * 校验参数
     * @author wubin
     * @date 2017/10/11
     **/
    private static void validateParams(String fileName, String latIndex, String lngIndex) {

        if(StringUtils.isBlank(fileName)
                || !fileName.trim().endsWith(".xlsx")) {
            throw new RuntimeException("暂时只支持xlsx格式的文件！");
        }
        if(!NumberUtils.isNumber(latIndex)
                || NumberUtils.toInt(latIndex) < 0) {
            throw new RuntimeException("纬度所在列数参数输入有误！");
        }
        if(!NumberUtils.isNumber(lngIndex)
                || NumberUtils.toInt(lngIndex) < 0) {
            throw new RuntimeException("经度所在列数参数输入有误！");
        }
    }
}
