package com.raythonsoft.sso.util;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by Anur IjuoKaruKas on 2018/4/9.
 * Description :
 */
public class UrlUtil {

    public static String getEncodeUrlWithParam(HttpServletRequest httpServletRequest) {
        StringBuffer urlBuffer = httpServletRequest.getRequestURL();

        boolean first = true;
        for (Map.Entry<String, String[]> entry : httpServletRequest.getParameterMap().entrySet()) {
            for (int i = 0; i < entry.getValue().length; i++) {
                if (first) {
                    urlBuffer.append("?");
                    first = false;
                } else {
                    urlBuffer.append("&");
                }
                urlBuffer.append(entry.getKey()).append("=").append(entry.getValue()[i]);
            }
        }
        String backUrl = "";
        try {
            backUrl = URLEncoder.encode(urlBuffer.toString(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return backUrl;
    }


    public static String addParam(String url, String paramKey, String paramValue) {
        StringBuffer stringBuffer = new StringBuffer(url);
        if (url.contains("?")) {
            stringBuffer.append("&");
        } else {
            stringBuffer.append("?");
        }
        return stringBuffer.append(paramKey).append("=").append(paramValue).toString();
    }
}
