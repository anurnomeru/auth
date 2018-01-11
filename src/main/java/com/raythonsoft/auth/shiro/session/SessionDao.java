package com.raythonsoft.auth.shiro.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;

import java.io.Serializable;

/**
 * Created by Anur IjuoKaruKas on 2018/1/10.
 * Description :
 */
public class SessionDao extends CachingSessionDAO {

    /**
     * 更新会话
     *
     * @param session
     */
    @Override
    protected void doUpdate(Session session) {
    }

    /**
     * 删除会话
     *
     * @param session
     */
    @Override
    protected void doDelete(Session session) {
    }

    /**
     * 创建会话
     *
     * @param session
     * @return
     */
    @Override
    protected Serializable doCreate(Session session) {
        return null;
    }

    /**
     * 获取会话
     *
     * @param serializable
     * @return
     */
    @Override
    protected Session doReadSession(Serializable serializable) {
        return null;
    }
}
