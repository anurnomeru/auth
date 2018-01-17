package com.raythonsoft.sso.filter;


import com.raythonsoft.sso.common.SsoConstant;
import org.apache.shiro.session.Session;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Created by Anur IjuoKaruKas on 2018/1/9.
 * Description : 强制退出会话过滤器
 */
public class ForceLogoutFilter extends AccessControlFilter {

    /**
     * 即是否允许访问，返回true表示允许；
     *
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        Session session = getSubject(request, response).getSession(false);
        if (session == null) {
            return true;
        }
        // 强制退出时，直接拒绝访问
        boolean forceLogout = session.getAttribute(SsoConstant.FORCE_LOGOUT) == null;
        return forceLogout;
    }


    /**
     * 表示访问拒绝时是否自己处理，如果返回true表示自己不处理且继续拦截器链执行，返回false表示自己已经处理了（比如重定向到另一个页面）。
     * 在这里，拒绝后返回登录页
     *
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        getSubject(servletRequest, servletResponse).logout();
        String loginUrl = getLoginUrl() + (getLoginUrl().contains("?") ? "&" : "?") + "forceLogout=1";
        WebUtils.issueRedirect(servletRequest, servletResponse, loginUrl);// 重定向到登录页
        return false;
    }
}
