package com.ishansong.tools.businessCircle.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.UnsupportedEncodingException;

/**
 * 文件路径工具类
 * @author wubin
 * @date 2017/10/2
 **/
public class PathUtil {

    private static final Logger logger = LoggerFactory.getLogger(PathUtil.class);

    private static String BUSINESS_CIRCLES_FILE_PATH = null;

    public static String getJarPath() throws UnsupportedEncodingException {

        String jarWholePath = PathUtil.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        jarWholePath = java.net.URLDecoder.decode(jarWholePath, "UTF-8");
        String jarPath = new File(jarWholePath).getParentFile().getAbsolutePath() + "/";
        return jarPath;
    }

    public static String getBusinessCirclesFilePath() throws Exception {
        if(BUSINESS_CIRCLES_FILE_PATH == null) {
            BUSINESS_CIRCLES_FILE_PATH = PropertiesUtil.getValue("businessCircle.datas.filePath");
        }
        logger.info(BUSINESS_CIRCLES_FILE_PATH);
        return BUSINESS_CIRCLES_FILE_PATH;
    }

    public static String cutFileSuffix(String filePath) {
        return filePath.substring(0, filePath.lastIndexOf("."));
    }
}
