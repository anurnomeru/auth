package com.raythonsoft.sso.util;

import com.github.pagehelper.util.StringUtil;
import com.raythonsoft.common.constant.AuthConstant;

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

    public static String getUrlWithOutCodeAndName(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        StringBuilder sb = new StringBuilder();
        Map<String, String[]> parameterMap = request.getParameterMap();

        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            if (!AuthConstant.REQUEST_PARAM_OSS_CODE.equals(entry.getKey()) && !AuthConstant.REQUEST_PARAM_OSS_USERNAME.equals(entry.getKey())) {
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
