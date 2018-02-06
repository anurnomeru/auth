package com.raythonsoft.sso.repository;

import com.raythonsoft.sso.model.CustomSession;
import com.raythonsoft.sso.session.OnlineStatusEnum;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Anur IjuoKaruKas on 2018/1/16.
 * Description : 面向redis的直接操作
 */
public interface SessionOperationRepository {

    /**
     * 获取保存在redis的 shiro session
     *
     * @param sessionId
     * @return
     */
    CustomSession getShiroSession(Serializable sessionId);

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
     * 更新在线状态
     *
     * @param sessionId
     * @param onlineStatusEnum
     */
    void updateShiroSessionStatus(Serializable sessionId, OnlineStatusEnum onlineStatusEnum);


    /**
     * 删除保存在redis的 shiro session
     *
     * @param sessionId
     */
    void deleteShiroSession(Serializable sessionId);

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
     * 将 sessionId 从全局session中移除
     *
     * @param sessionId
     * @param count
     */
    void leftRemFromServerSessionIds(String sessionId, Integer count);

    /**
     * 将 sessionId 存入全局session中移除
     *
     * @param sessionId
     */
    void leftPushIntoServerSessionId(Serializable sessionId);

    /**
     * 获取当前的总服务器数
     *
     * @return
     */
    long getServerSessionCount();

    /**
     * 获取当前服务器的id
     *
     * @param offset
     * @param limit
     * @return
     */
    List<Object> getServerSessionList(int offset, int limit);
}
