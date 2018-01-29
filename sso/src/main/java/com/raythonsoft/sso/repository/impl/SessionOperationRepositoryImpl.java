package com.raythonsoft.sso.repository.impl;

import com.raythonsoft.sso.model.CustomSession;
import com.raythonsoft.sso.repository.CodeRedisRepository;
import com.raythonsoft.sso.repository.SessionIdGenerator;
import com.raythonsoft.sso.repository.SessionOperationRepository;
import com.raythonsoft.sso.session.OnlineStatusEnum;
import com.raythonsoft.sso.util.SerializableUtil;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by Anur IjuoKaruKas on 2018/1/16.
 * Description :
 */
@Repository
@Log4j
public class SessionOperationRepositoryImpl implements SessionOperationRepository {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private SessionIdGenerator sessionIdGenerator;

    @Autowired
    private CodeRedisRepository codeRedisRepository;

    @Override
    public CustomSession getShiroSession(Serializable sessionId) {
        CustomSession customSession = (CustomSession) SerializableUtil.deserialize((String) redisTemplate.opsForValue().get(sessionIdGenerator.genShiroSessionId(sessionId)));
        log.debug(String.format("doReadSession >>>>> sessionId=%s", sessionId));
        return customSession;
    }

    @Override
    public void saveOrUpdateShiroSession(CustomSession customSession, Serializable sessionId, boolean isCreate) {
        redisTemplate.opsForValue().set(sessionIdGenerator.genShiroSessionId(sessionId), SerializableUtil.serialize(customSession), customSession.getTimeout(), TimeUnit.MILLISECONDS);
        if (isCreate) {
            log.debug(String.format("doCreate >>>>> sessionId=%s", sessionId));
        } else {
            log.debug(String.format("doUpdate >>>>> sessionId=%s", sessionId));
        }
        log.debug(String.format("status >>>>> onlineStatus=%s", customSession.getOnlineStatusEnum()));
    }

    @Override
    public void updateShiroSessionStatus(Serializable sessionId, OnlineStatusEnum onlineStatusEnum) {
        CustomSession customSession = this.getShiroSession(sessionId);
        customSession.setOnlineStatusEnum(onlineStatusEnum);
        this.saveOrUpdateShiroSession(customSession, sessionId, false);
    }

    @Override
    public void deleteShiroSession(Serializable sessionId) {
        redisTemplate.delete(sessionIdGenerator.genShiroSessionId(sessionId));
        log.debug(String.format("doDelete >>>>> sessionId=%s", sessionId));
    }

    @Override
    public void deleteClientSessionId(String sessionId) {
        // 局部会话code
        String code = (String) redisTemplate.opsForValue().get(sessionIdGenerator.genClientSessionId(sessionId));
        // 删除局部会话
        redisTemplate.delete(sessionIdGenerator.genClientSessionId(sessionId));
        // 删除同一个code（server）注册的局部会话
        redisTemplate.opsForSet().remove(sessionIdGenerator.genClientSessionIdsCodeParamCode(code), sessionId);
    }

    @Override
    public void deleteServerSessionId(String sessionId) {
        // 全局会话code
        String code = (String) redisTemplate.opsForValue().get(sessionIdGenerator.genServerSessionId(sessionId));
        // 清除全局会话
        redisTemplate.delete(sessionIdGenerator.genServerSessionId(sessionId));
        // 清除code校验值
        redisTemplate.delete(sessionIdGenerator.genServerCode(code));

            /*
             清除该server下所有局部会话
             */

        // server下的client会话【们】
        String clientSessionIdsCode = sessionIdGenerator.genClientSessionIdsCodeParamCode(code);
        Set<Object> clientSessionIds = redisTemplate.opsForSet().members(clientSessionIdsCode);
        for (Object clientSessionId : clientSessionIds) {
            // server下的client会话
            this.deleteClientSessionId(String.valueOf(clientSessionId));
        }

        codeRedisRepository.scardCode(code);// 删除Server后打印一下日志

        this.leftRemFromServerSessionIds(sessionId, 1);// leftRem 从全局会话中，删除这个sessionId
    }

    /**
     * 【 内部方法 不可直接将全局会话删除 】
     * 将 sessionId 从 Redis 全局 session 列表左 rem
     *
     * @param sessionId
     * @param count
     */
    private void leftRemFromServerSessionIds(String sessionId, Integer count) {
        // leftRem 从全局会话中，删除这个sessionId
        redisTemplate.opsForList().remove(sessionIdGenerator.genServerSessionIds(), count, sessionId);
    }

    @Override
    public void leftPushIntoServerSessionId(Serializable sessionId) {
        redisTemplate.opsForList().leftPush(sessionIdGenerator.genServerSessionIds(), sessionId);
    }

    @Override
    public long getServerSessionCount() {
        return redisTemplate.opsForList().size(sessionIdGenerator.genServerSessionIds());
    }

    @Override
    public List<Object> getServerSessionList(int offset, int limit) {
        return redisTemplate.opsForList().range(sessionIdGenerator.genServerSessionIds(), offset, limit);
    }
}
