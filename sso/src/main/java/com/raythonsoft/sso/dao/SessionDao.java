package com.raythonsoft.sso.dao;

import com.raythonsoft.common.constant.AuthConstant;
import com.raythonsoft.common.constant.SsoTypeEnum;
import com.raythonsoft.sso.common.SsoConstant;
import com.raythonsoft.sso.model.CustomSession;
import com.raythonsoft.sso.model.SessionPageInfo;
import com.raythonsoft.sso.repository.SessionOperationRepository;
import com.raythonsoft.sso.session.OnlineStatusEnum;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * Created by Anur IjuoKaruKas on 2018/1/10.
 * Description :
 */
@Repository
public class SessionDao extends EnterpriseCacheSessionDAO {

    @Autowired
    private SessionOperationRepository sessionOperationRepository;


    /**
     * 创建会话
     *
     * @param session
     * @return
     */
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);

        sessionOperationRepository.saveOrUpdateShiroSession((CustomSession) session, sessionId, true);
        return sessionId;
    }

    /**
     * 获取会话
     *
     * @param sessionId
     * @return
     */
    @Override
    protected Session doReadSession(Serializable sessionId) {
        Session session = sessionOperationRepository.getShiroSession(sessionId);
        return session;
    }

    /**
     * 更新会话
     *
     * @param session
     */
    @Override
    protected void doUpdate(Session session) {
        // 如果会话过期/停止 没必要再更新了
        if (session instanceof ValidatingSession && !((ValidatingSession) session).isValid()) {
            return;
        }

        // 更新访问时间
        CustomSession customSession = (CustomSession) session;
        CustomSession cacheSession = (CustomSession) doReadSession(session.getId());
        if (null != cacheSession) {
            customSession.setOnlineStatusEnum(cacheSession.getOnlineStatusEnum());
            customSession.setAttribute(SsoConstant.FORCE_LOGOUT, cacheSession.getAttribute(SsoConstant.FORCE_LOGOUT));
        }

        sessionOperationRepository.saveOrUpdateShiroSession(customSession, customSession.getId(), false);
    }

    /**
     * 删除会话
     *
     * @param session
     */
    @Override
    protected void doDelete(Session session) {
        String sessionId = String.valueOf(session.getId());
        String ssoType = String.valueOf(session.getAttribute(AuthConstant.SSO_PROPERTY.SSO_PROPERTY_TYPE));// 判断当前session是客户端还是服务器端

        if (SsoTypeEnum.CLIENT.getName().equals(ssoType)) {
            sessionOperationRepository.deleteClientSessionId(sessionId);
        }

        if (SsoTypeEnum.SERVER.getName().equals(ssoType)) {
            sessionOperationRepository.deleteServerSessionId(sessionId);
        }

        // 删除shiro下的Session
        sessionOperationRepository.deleteShiroSession(sessionId);
    }

    /**
     * 获取带分页的未超时session
     *
     * @param offset
     * @param limit
     * @return
     */
    public SessionPageInfo getActiveSessions(int offset, int limit) {
        long total = sessionOperationRepository.getServerSessionCount();

        // fixme 分页存疑
        List<Object> sessionIdList = sessionOperationRepository.getServerSessionList(offset, limit);
        List<Session> sessionList = new ArrayList<>();
        for (Object sessionId : sessionIdList) {
            Session session = sessionOperationRepository.getShiroSession((String) sessionId);
            // 将过期的 session 移除
            if (null == session) {
                sessionOperationRepository.leftRemFromServerSessionIds((String) sessionId, 1);
                total = total - 1;
                continue;
            }
            sessionList.add(session);
        }

        return SessionPageInfo.builder().rows(sessionList).total(total).build();
    }

    /**
     * 强制使某些session退出
     *
     * @param stringSet
     * @return
     */
    public int forceLogout(Set<String> stringSet) {
        for (String sessionId : stringSet) {
            CustomSession customSession = sessionOperationRepository.getShiroSession(sessionId);
            customSession.setOnlineStatusEnum(OnlineStatusEnum.FORCE_LOGOUT);
            customSession.setAttribute(SsoConstant.FORCE_LOGOUT, SsoConstant.FORCE_LOGOUT);// FIXME: 2018/1/16 存疑
            sessionOperationRepository.saveOrUpdateShiroSession(customSession, customSession.getId(), false);
        }
        return stringSet.size();
    }

    /**
     * 更改session状态
     *
     * @param sessionId
     * @param onlineStatusEnum
     */
    public void updateStatus(Serializable sessionId, OnlineStatusEnum onlineStatusEnum) {
        CustomSession customSession = sessionOperationRepository.getShiroSession(sessionId);
        if (null == customSession) {
            return;
        }
        customSession.setOnlineStatusEnum(onlineStatusEnum);
        sessionOperationRepository.saveOrUpdateShiroSession(customSession, sessionId, false);
    }

}
