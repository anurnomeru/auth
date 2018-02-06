package com.raythonsoft.common.util;

/**
 * Created by Anur IjuoKaruKas on 2018/2/6.
 * Description :
 */
public class StringUtils {

    public static boolean isEmpty(String toTest){
        return toTest == null || toTest.length() == 0 || "null".equals(toTest);
    }

    public static boolean isNotEmpty(String toTest){
        return !(toTest == null || toTest.length() == 0 || "null".equals(toTest));
    }
}
