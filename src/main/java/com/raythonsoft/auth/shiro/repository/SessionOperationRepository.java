package com.raythonsoft.auth.shiro.repository;

import org.apache.shiro.session.Session;

import java.io.Serializable;

/**
 * Created by Anur IjuoKaruKas on 2018/1/16.
 * Description : 面向redis的直接操作
 */
public interface SessionOperationRepository {

    /**
     * 内部方法，用于删除client会话
     *
     * @param sessionId
     */
    void deleteClientSessionId(String sessionId);

    /**
     * 内部方法，用于删除server会话
     *
     * @param sessionId
     */
    void deleteServerSessionId(String sessionId);

    /**
     * 保存sessionId ==> key
     * session ==> value
     *
     * @param session
     * @param sessionId
     */
    void saveShiroSession(Session session, Serializable sessionId);

    /**
     * 获取保存在redis的 shiro session
     *
     * @param sessionId
     * @return
     */
    Session getShiroSession(Serializable sessionId);

    /**
     * 删除保存在redis的 shiro session
     *
     * @param sessionId
     */
    void deleteShiroSession(Serializable sessionId);

    /**
     * 生成shiroSessionId
     *
     * @param sessionId
     * @return
     */
    String genShiroSessionId(Serializable sessionId);
}
