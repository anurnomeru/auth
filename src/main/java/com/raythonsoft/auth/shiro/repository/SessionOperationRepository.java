package com.raythonsoft.auth.shiro.repository;

import com.raythonsoft.auth.shiro.model.CustomSession;
import org.apache.shiro.session.Session;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Anur IjuoKaruKas on 2018/1/16.
 * Description : 面向redis的直接操作
 */
public interface SessionOperationRepository {

    /**
     * 生成shiroSessionId
     *
     * @param sessionId
     * @return
     */
    String genShiroSessionId(Serializable sessionId);

    /**
     * 保存sessionId ==> key
     * session ==> value
     *
     * @param customSession
     * @param sessionId
     * @param isCreate      打印日志用
     */
    void saveOrUpdateShiroSession(CustomSession customSession, Serializable sessionId, boolean isCreate);

    /**
     * 删除保存在redis的 shiro session
     *
     * @param sessionId
     */
    void deleteShiroSession(Serializable sessionId);

    /**
     * 获取保存在redis的 shiro session
     *
     * @param sessionId
     * @return
     */
    CustomSession getShiroSession(Serializable sessionId);

    /**
     * 用于删除client会话
     *
     * @param sessionId
     */
    void deleteClientSessionId(String sessionId);

    /**
     * 用于删除server会话
     *
     * @param sessionId
     */
    void deleteServerSessionId(String sessionId);

    /**
     * 将sessionId从某服务列表删掉
     *
     * @param sessionId
     */
    void deleteSessionFromServerSessionIds(String sessionId);

    /**
     * 获取当前的总服务器数
     *
     * @return
     */
    long getServerCount();

    /**
     * 获取当前服务器的id
     *
     * @return
     */
    List<Object> getServerSession(int offset, int limit);
}
