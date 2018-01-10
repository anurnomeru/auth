package com.raythonsoft.auth.shiro.factory;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionFactory;

/**
 * Created by Anur IjuoKaruKas on 2018/1/9.
 * Description :
 */
public class CustomFactory implements SessionFactory {
    @Override
    public Session createSession(SessionContext sessionContext) {
        return null;
    }
}
