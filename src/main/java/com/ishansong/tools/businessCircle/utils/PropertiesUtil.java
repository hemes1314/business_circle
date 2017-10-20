package com.ishansong.tools.businessCircle.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * properties读取工具类
 * @author wubin
 * @date 2017/10/02
 **/
public class PropertiesUtil {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    private static final String CONF_PATH = "conf.properties";

    private volatile static Properties props = null;

    private PropertiesUtil(){}

    private static Properties getProps() throws Exception {
        if(props == null) {
            synchronized (PropertiesUtil.class) {
                if(props == null) {

                    props = new Properties();
                    String confFilePath = PathUtil.getJarPath() + CONF_PATH;
                    InputStream in = new FileInputStream(confFilePath);
                    props.load(in);
                }
            }
        }
        return props;
    }

    public static String getValue(String key) throws Exception {

        return getProps().getProperty(key);
    }

    public static String getValue(String key, String defaultValue) throws Exception {

        return getProps().getProperty(key, defaultValue);
    }

    public static void main(String[] args) throws Exception {

		String value = PropertiesUtil.getValue("businessCircle.datas.filePath");
		System.out.println("111"+value+"222");
    }
}