package com.raythonsoft.auth.shiro.listener;

import lombok.extern.log4j.Log4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

/**
 * Created by Anur IjuoKaruKas on 2018/1/9.
 * Description :
 */
@Log4j
public class CustomSessionListener implements SessionListener {
    @Override
    public void onStart(Session session) {
        log.debug("会话创建：" + session.getId());
    }

    @Override
    public void onStop(Session session) {
        log.debug("会话停止：" + session.getId());
    }

    @Override
    public void onExpiration(Session session) {
        log.debug("会话过期：" + session.getId());
    }
}
