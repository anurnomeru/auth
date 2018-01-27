package com.raythonsoft.sso.repository.impl;

import com.raythonsoft.sso.model.CustomSession;
import com.raythonsoft.sso.repository.CodeRedisRepository;
import com.raythonsoft.sso.repository.SessionIdGenerator;
import com.raythonsoft.sso.repository.SessionOperationRepository;
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

        Object o = redisTemplate.opsForValue().get(sessionIdGenerator.genShiroSessionId(sessionId));
        CustomSession customSession = (CustomSession) redisTemplate.opsForValue().get(sessionIdGenerator.genShiroSessionId(sessionId));
        log.info(String.format("doReadSession >>>>> sessionId=%s", sessionId));
        return customSession;
    }

    @Override
    public void saveOrUpdateShiroSession(CustomSession customSession, Serializable sessionId, boolean isCreate) {
        redisTemplate.opsForValue().set(sessionIdGenerator.genShiroSessionId(sessionId), customSession, customSession.getTimeout(), TimeUnit.MILLISECONDS);
        if (isCreate) {
            log.info(String.format("doCreate >>>>> sessionId=%s", sessionId));
        } else {
            log.info(String.format("doUpdate >>>>> sessionId=%s", sessionId));
        }
        log.info(String.format("status >>>>> onlineStatus=%s", customSession.getOnlineStatusEnum()));
    }

    @Override
    public void deleteShiroSession(Serializable sessionId) {
        redisTemplate.delete(sessionIdGenerator.genShiroSessionId(sessionId));
        log.info(String.format("doDelete >>>>> sessionId=%s", sessionId));
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
        //
        //  RedisUtil.lrem(ZHENG_UPMS_SERVER_SESSION_IDS, 1, sessionId)
        //  少了维护会话id列表
        //
        // FIXME: 2018/1/26
        // 将session从ServerSessionIds中删除
        this.deleteSessionFromServerSessionIds(sessionId);
    }

    @Override
    public void deleteSessionFromServerSessionIds(String sessionId) {
        // 维护会话id列表，就是把它删掉
        redisTemplate.opsForSet().remove(sessionIdGenerator.genServerSessionIds(), sessionId);
    }

    @Override
    public long getServerCount() {
        return redisTemplate.opsForList().size(sessionIdGenerator.genServerSessionIds());
    }

    @Override
    public List<Object> getServerSession(int offset, int limit) {
        return redisTemplate.opsForList().range(sessionIdGenerator.genServerSessionIds(), offset, limit);
    }
}
