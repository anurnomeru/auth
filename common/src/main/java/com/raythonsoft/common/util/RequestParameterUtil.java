package com.raythonsoft.common.util;

import com.github.pagehelper.util.StringUtil;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Anur IjuoKaruKas on 2018/1/18.
 * Description :
 */
public class RequestParameterUtil {

    private static final String CODE = "oss_code";
    private static final String USERNAME = "oss_username";

    public static String getUrlWithOutCodeAndName(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        StringBuilder sb = new StringBuilder();
        Map<String, String[]> parameterMap = request.getParameterMap();

        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            if (!CODE.equals(entry.getKey()) && !USERNAME.equals(entry.getKey())) {
                sb.append(String.format("&%s=%s", entry.getKey(), entry.getValue()[0]));
            }
        }

        String params = sb.toString();
        if (StringUtil.isNotEmpty(params)) {
            url.append(params.replaceFirst("&", "?"));
        }

        return url.toString();
    }
}
