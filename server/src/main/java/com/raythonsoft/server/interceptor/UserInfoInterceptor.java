package com.raythonsoft.server.interceptor;

import com.raythonsoft.sso.model.AuthUser;
import com.raythonsoft.sso.service.AuthUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Anur IjuoKaruKas on 2018/1/29.
 * Description : 获取登陆信息
 */
public class UserInfoInterceptor extends HandlerInterceptorAdapter {

    private static final String REQUEST_TYPE = "X-Requested-With";
    private static final String REQUEST_AJAX = "XMLHttpRequest";

    @Autowired
    private AuthUserService authUserService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getHeader(REQUEST_TYPE) == null || !REQUEST_AJAX.equals(request.getHeader(REQUEST_TYPE))) {
            Subject subject = SecurityUtils.getSubject();
            String username = (String) subject.getPrincipal();
            AuthUser user = authUserService.findBy("username", username);
            request.setAttribute("user", user);
        }
        return super.preHandle(request, response, handler);
    }
}
