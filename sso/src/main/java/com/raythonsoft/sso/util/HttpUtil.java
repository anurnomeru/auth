package com.raythonsoft.sso.util;

import java.lang.reflect.Field;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.RequestFacade;
import org.apache.tomcat.util.buf.MessageBytes;
import org.apache.tomcat.util.http.MimeHeaders;

/**
 * Created by Anur IjuoKaruKas on 2019/5/31
 */
public class HttpUtil {

    private static Field request;

    private static Field request1;

    private static Field coyoteRequest;

    static {
        try {
            request = ServletRequestWrapper.class.getDeclaredField("request");
            request.setAccessible(true);
            request1 = RequestFacade.class.getDeclaredField("request");
            request1.setAccessible(true);
            coyoteRequest = Request.class.getDeclaredField("coyoteRequest");
            coyoteRequest.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setHeader(HttpServletRequest httpServletRequest, String k, String v) throws IllegalAccessException {
        RequestFacade requestFacade = (RequestFacade) request.get(httpServletRequest);
        Request request2 = (Request) request1.get(requestFacade);
        org.apache.coyote.Request request3 = (org.apache.coyote.Request) coyoteRequest.get(request2);
        MimeHeaders mimeHeaders = request3.getMimeHeaders();
        MessageBytes val = mimeHeaders.addValue(k);
        val.setString(v);
    }
}
