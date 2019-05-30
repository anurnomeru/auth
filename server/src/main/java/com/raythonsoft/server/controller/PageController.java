package com.raythonsoft.server.controller;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicInteger;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.raythonsoft.common.util.StringUtils;
import com.raythonsoft.sso.common.SsoConstant;
import com.raythonsoft.sso.service.SessionService;
import com.raythonsoft.sso.util.UrlUtil;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.RequestFacade;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.tomcat.util.buf.MessageBytes;
import org.apache.tomcat.util.http.MimeHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Anur IjuoKaruKas on 2018/2/7.
 * Description :
 */
@Controller
@RequestMapping
public class PageController {

    @Autowired
    private SessionService sessionService;

    AtomicInteger atomicInteger = new AtomicInteger();

    static Field request;

    static Field request1;

    static Field coyoteRequest;

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

    @GetMapping("/")
    public String index(HttpServletRequest httpServletRequest, HttpServletResponse response, String backUrl) throws IllegalAccessException, NoSuchFieldException, IOException {
        setHeader(httpServletRequest, "k", "v");

        response.addHeader("萌狮" + atomicInteger.get(), "lalalal");
        if (atomicInteger.getAndAdd(1) < 10) {
            response.sendRedirect("redirect:/");
            return null;
        }

        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        // 如果请求来自客户端
        if (StringUtils.isNotEmpty(backUrl)) {
            if (subject.isAuthenticated()) {
                // 已经登陆，则直接带着code 回跳
                return String.format("redirect:%s", UrlUtil.addParam(backUrl, "checkCode", sessionService.getCheckCodeBySessionId(String.valueOf(session.getId()))));
            } else {
                // 没有登陆，则去登陆
                subject.getSession()
                       .setAttribute(SsoConstant.BACK_URL, backUrl);
            }
        } else {
            if (subject.isAuthenticated()) {
                return "redirect:/home";
            }
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "index";
    }

    @GetMapping("/home")
    public String home() {
        return "login_success";
    }
}
