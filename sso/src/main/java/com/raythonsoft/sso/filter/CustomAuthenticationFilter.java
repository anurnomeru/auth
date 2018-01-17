package com.raythonsoft.sso.filter;

import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;


/**
 * Created by Anur IjuoKaruKas on 2018/1/9.
 * Description : 认证 filter
 */
public class CustomAuthenticationFilter extends AuthenticationFilter {

    /**
     * 是否通过认证
     *
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        Subject subject = getSubject(request, response);
        Session session = subject.getSession();

        return super.isAccessAllowed(request, response, mappedValue);
    }

    /**
     * 认证通过后，做什么
     *
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        return false;
    }
}
