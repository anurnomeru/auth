package com.raythonsoft.auth.shiro.session;

import com.raythonsoft.auth.shiro.properties.ShiroProperties;
import lombok.extern.log4j.Log4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Created by Anur IjuoKaruKas on 2018/1/10.
 * Description :
 */
@Log4j
@Component
public class SessionDao extends CachingSessionDAO {

    @Autowired
    private RedisTemplate redisTemplate;

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
        // fixme 不知道这个具体是什么意思
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);

        redisTemplate.opsForValue().set(String.format("%s_%s", shiroProperties.getAuthShiroSessionId(), sessionId), session, session.getTimeout(), TimeUnit.MILLISECONDS);
        log.info("doCreate >>>>> sessionId={}", (Throwable) sessionId);
        return sessionId;
    }

    /**
     * 获取会话
     *
     * @param serializable
     * @return
     */
    @Override
    protected Session doReadSession(Serializable serializable) {
        Session session = (Session) redisTemplate.opsForValue().get(serializable);
        return null;
    }

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
}
