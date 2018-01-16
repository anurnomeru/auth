package com.raythonsoft.auth.shiro.session;

import com.raythonsoft.auth.common.AuthConstant;
import com.raythonsoft.auth.common.ShiroTypeEnum;
import com.raythonsoft.auth.shiro.properties.ShiroProperties;
import lombok.extern.log4j.Log4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.raythonsoft.auth.common.ProjectConstant.SHIRO_TYPE;

/**
 * Created by Anur IjuoKaruKas on 2018/1/10.
 * Description :
 */
@Log4j
@Component
public class SessionDao extends EnterpriseCacheSessionDAO {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private ShiroProperties shiroProperties;

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

        redisTemplate.opsForValue().set(String.format(AuthConstant.ID_FORM, shiroProperties.getAuthShiroSessionId(), sessionId), session, session.getTimeout(), TimeUnit.MILLISECONDS);
        log.info(String.format("doCreate >>>>> sessionId=%s", sessionId));
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
        Session session = (Session) redisTemplate.opsForValue().get(String.format(AuthConstant.ID_FORM, shiroProperties.getAuthShiroSessionId(), sessionId));
        log.info(String.format("doReadSession >>>>> sessionId=%s", sessionId));
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
        redisTemplate.opsForValue().set(String.format(AuthConstant.ID_FORM, shiroProperties.getAuthShiroSessionId(), customSession.getId()), session, session.getTimeout(), TimeUnit.MILLISECONDS);

        log.info(String.format("doUpdate >>>>> sessionId=%s", customSession.getId()));
    }

    /**
     * 删除会话
     *
     * @param session
     */
    @Override
    protected void doDelete(Session session) {
        // FIXME: 2018/1/16 还有很多
        String sessionId = String.valueOf(session.getId());
        String shiroType = String.valueOf(session.getAttribute(SHIRO_TYPE));// 判断当前session是客户端还是服务器端

        if (ShiroTypeEnum.CLIENT.name().equals(shiroType)) {
            deleteClientSessionId(sessionId);
        }

        if (ShiroTypeEnum.SERVER.name().equals(shiroType)) {
            deleteServerSessionId(sessionId);
        }

        redisTemplate.delete(String.format(AuthConstant.ID_FORM, shiroProperties.getAuthShiroSessionId(), sessionId));
    }

    /**
     * 内部方法，用于删除client会话
     *
     * @param sessionId
     */
    private void deleteClientSessionId(String sessionId) {
        // 局部会话code
        String code = (String) redisTemplate.opsForValue().get(String.format(AuthConstant.ID_FORM, shiroProperties.getAuthClientSessionId(), sessionId));
        // 删除局部会话
        redisTemplate.delete(code);
        // 删除同一个code（server）注册的局部会话
        redisTemplate.opsForSet().remove(String.format(AuthConstant.ID_FORM, shiroProperties.getAuthClientSessionIds(), code), sessionId);
    }

    /**
     * 内部方法，用于删除server会话
     *
     * @param sessionId
     */
    private void deleteServerSessionId(String sessionId) {
        // 全局会话code
        String code = (String) redisTemplate.opsForValue().get(String.format(AuthConstant.ID_FORM, shiroProperties.getAuthServerSessionId(), sessionId));
        // 清除全局会话
        redisTemplate.delete(code);
        // 清除code校验值
        redisTemplate.delete(String.format(AuthConstant.ID_FORM, shiroProperties.getAuthServerCode(), code));

            /*
             清除该server下所有局部会话
             */

        // server下的client会话【们】
        String clientSessionIdsCode = String.format(AuthConstant.ID_FORM, shiroProperties.getAuthClientSessionIds(), code);
        Set<Object> clientSessionIds = redisTemplate.opsForSet().members(clientSessionIdsCode);
        for (Object clientSessionId : clientSessionIds) {
            // server下的client会话
            deleteClientSessionId(String.valueOf(clientSessionId));
        }

        log.info(String.format("当前code=%s，对应的注册系统个数：%s个",
                code,
                redisTemplate.opsForSet().size(clientSessionIdsCode)
                )
        );

        // FIXME: 2018/1/16 what?????
        // 维护会话id列表，提供会话分页管理
        redisTemplate.opsForSet().remove(shiroProperties.getAuthServerSessionIds(), sessionId);
    }
}
