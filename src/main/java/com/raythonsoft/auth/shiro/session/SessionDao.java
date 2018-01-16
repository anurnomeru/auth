package com.raythonsoft.auth.shiro.session;

import com.raythonsoft.auth.common.AuthConstant;
import com.raythonsoft.auth.common.ShiroTypeEnum;
import com.raythonsoft.auth.shiro.model.CustomSession;
import com.raythonsoft.auth.shiro.model.SessionPageInfo;
import com.raythonsoft.auth.shiro.repository.SessionOperationRepository;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.raythonsoft.auth.common.ProjectConstant.SHIRO_TYPE;

/**
 * Created by Anur IjuoKaruKas on 2018/1/10.
 * Description :
 */
@Component
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
        // fixme 不知道这个具体是什么意思，大概从sessionFactory创建一个session
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);

        sessionOperationRepository.saveOrUpdateShiroSession(session, sessionId, true);
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
            customSession.setAttribute(AuthConstant.FORCE_LOGOUT, cacheSession.getAttribute(AuthConstant.FORCE_LOGOUT));
        }

        sessionOperationRepository.saveOrUpdateShiroSession(session, session.getId(), false);
    }

    /**
     * 删除会话
     *
     * @param session
     */
    @Override
    protected void doDelete(Session session) {
        String sessionId = String.valueOf(session.getId());
        String shiroType = String.valueOf(session.getAttribute(SHIRO_TYPE));// 判断当前session是客户端还是服务器端

        if (ShiroTypeEnum.CLIENT.name().equals(shiroType)) {
            sessionOperationRepository.deleteClientSessionId(sessionId);
        }

        if (ShiroTypeEnum.SERVER.name().equals(shiroType)) {
            sessionOperationRepository.deleteServerSessionId(sessionId);
        }

        // 删除shiro下的Session
        sessionOperationRepository.deleteShiroSession(sessionId);

    }

    public SessionPageInfo getActiveSessions(int offset, int limit) {
        long total = sessionOperationRepository.getServerCount();

        // fixme 分页存疑
        List<Object> sessionIdList = sessionOperationRepository.getServerSession(offset, limit);
        List<Session> sessionList = new ArrayList<>();
        for (Object sessionId : sessionIdList) {
            Session session = sessionOperationRepository.getShiroSession((String) sessionId);
            if (null == session) {
                sessionOperationRepository.deleteSessionFromServerSessionIds((String) sessionId);
                total = total - 1;
                continue;
            }
            sessionList.add(session);
        }

        return SessionPageInfo.builder().rows(sessionList).total(total).build();
    }

}
