package com.raythonsoft.auth.shiro.session;

import com.raythonsoft.auth.shiro.model.CustomSession;
import com.raythonsoft.auth.util.NetworkUtil;
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
        CustomSession session = new CustomSession();
        if (null != sessionContext && sessionContext instanceof WebSessionContext) {
            WebSessionContext webSessionContext = (WebSessionContext) sessionContext;
            HttpServletRequest request = (HttpServletRequest) webSessionContext.getServletRequest();
            if (null != request) {
                session.setHost(NetworkUtil.getIpAddress(request));
                session.setUserAgent(request.getHeader("User-Agent"));
            }
        }
        return session;
    }
}
