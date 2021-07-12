package com.pt.pmq.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author nate-pt
 * @date 2021/7/10 16:00
 * @Since 1.8
 * @Description properties工具类
 */
@Slf4j
public class PropertiesUtils {

    private static Properties properties;

    static {
        try {
            load();
        } catch (IOException e) {
            log.error("【pmq.properties 加载失败。。。。。。。。。。】");
            System.exit(0);
        }
    }

    private static void load() throws IOException {
        InputStream resourceAsStream = PropertiesUtils.class.getClassLoader().getResourceAsStream("pmq.properties");
        Properties properties = new Properties();
        properties.load(resourceAsStream);
        PropertiesUtils.properties = properties;
    }


    /**
     * 获取系统配置
     * @param key
     * @return
     */
    public static String get(String key){
        return properties.getProperty(key);
    }

}
