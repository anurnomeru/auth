package com.raythonsoft.auth.shiro.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionFactory;

/**
 * Created by Anur IjuoKaruKas on 2018/1/9.
 * Description :
 */
public class CustomSessionFactory implements SessionFactory {
    @Override
    public Session createSession(SessionContext sessionContext) {
        return null;
    }
}
