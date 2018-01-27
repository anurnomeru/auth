package com.raythonsoft.sso.session;

import com.raythonsoft.common.util.NetworkUtil;
import com.raythonsoft.sso.model.CustomSession;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.web.session.mgt.WebSessionContext;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Anur IjuoKaruKas on 2018/1/9.
 * Description :
 */
public class CustomSessionFactory implements SessionFactory {
    @Override
    public Session createSession(SessionContext sessionContext) {
        CustomSession customSession = new CustomSession();
        if (null != sessionContext && sessionContext instanceof WebSessionContext) {
            WebSessionContext webSessionContext = (WebSessionContext) sessionContext;
            HttpServletRequest request = (HttpServletRequest) webSessionContext.getServletRequest();
            if (null != request) {
                customSession.setHost(NetworkUtil.getIpAddress(request));
                customSession.setUserAgent(request.getHeader("User-Agent"));
            }
        }
        return customSession;
    }
}
