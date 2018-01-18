package com.raythonsoft.common.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Anur IjuoKaruKas on 2018/1/18.
 * Description : 读取文件
 */
public class PropertiesFileUtil {

    private static Map<String, PropertiesFileUtil> propertiesFileUtilMap = new HashMap<>();// 缓存读取的文件
    private Long loadTimeStamp = null;
    private ResourceBundle resourceBundle = null;
    private static final String NAME = "config";
    private static final Integer TIME_OUT = 60 * 1000;

    private PropertiesFileUtil(String name) {
        this.loadTimeStamp = System.currentTimeMillis();
        this.resourceBundle = ResourceBundle.getBundle(name);
    }

    public static synchronized PropertiesFileUtil getInstance() {
        return getInstance(NAME);
    }

    public static synchronized PropertiesFileUtil getInstance(String name) {
        PropertiesFileUtil propertiesFileUtil = propertiesFileUtilMap.get(name);
        if (null == propertiesFileUtil) {
            propertiesFileUtil = new PropertiesFileUtil(name);
            propertiesFileUtilMap.put(name, propertiesFileUtil);
        }

        if (System.currentTimeMillis() - propertiesFileUtil.loadTimeStamp > TIME_OUT) {
            propertiesFileUtil = new PropertiesFileUtil(name);
            propertiesFileUtilMap.put(name, propertiesFileUtil);
        }
        return propertiesFileUtil;
    }

    public <E> E get(String key) {
        try {
            return (E) this.resourceBundle.getString(key);
        } catch (Exception e) {
            return null;
        }
    }

    public Long getLoadTimeStamp() {
        return this.loadTimeStamp;
    }
}
