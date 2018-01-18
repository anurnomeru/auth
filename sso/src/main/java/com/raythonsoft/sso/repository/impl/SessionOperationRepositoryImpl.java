package com.raythonsoft.sso.repository.impl;

import com.raythonsoft.sso.common.SsoConstant;
import com.raythonsoft.sso.model.CustomSession;
import com.raythonsoft.sso.properties.ShiroProperties;
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
    private ShiroProperties shiroProperties;

    @Override
    public String genShiroSessionId(Serializable sessionId) {
        return String.format(SsoConstant.ID_FORM, shiroProperties.getAuthShiroSessionId(), sessionId);
    }

    @Override
    public String genClientSessionId(Serializable sessionId) {
        return String.format(SsoConstant.ID_FORM, shiroProperties.getAuthClientSessionId(), sessionId);
    }

    @Override
    public String genClientSessionIds(String code) {
        return String.format(SsoConstant.ID_FORM, shiroProperties.getAuthClientSessionId(), code);
    }

    @Override
    public String genServerSessionId(Serializable sessionId) {
        return String.format(SsoConstant.ID_FORM, shiroProperties.getAuthServerSessionId(), sessionId);
    }

    @Override
    public String genServerSessionIds() {
        return shiroProperties.getAuthServerSessionIds();
    }

    @Override
    public String genServerCode(String code) {
        return String.format(SsoConstant.ID_FORM, shiroProperties.getAuthServerCode(), code);
    }

    @Override
    public String getCodeByGenningSessionId(String genningSessionId) {
        return String.valueOf(redisTemplate.opsForValue().get(genningSessionId));
    }

    @Override
    public void setCodeByGenningSessionId(String genningSessionId, String cacheCode, Integer timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(genningSessionId, cacheCode, timeout, timeUnit);
    }

    @Override
    public void expireCodeByGenningSessionId(String genningSessionId, Integer timeout, TimeUnit timeUnit) {
        redisTemplate.expire(genningSessionId, timeout, timeUnit);
    }

////////////////////////////////// 基础操作分界线

    @Override
    public CustomSession getShiroSession(Serializable sessionId) {
        CustomSession customSession = (CustomSession) redisTemplate.opsForValue().get(this.genShiroSessionId(sessionId));
        log.info(String.format("doReadSession >>>>> sessionId=%s", sessionId));
        return customSession;
    }

    @Override
    public void saveOrUpdateShiroSession(CustomSession customSession, Serializable sessionId, boolean isCreate) {
        redisTemplate.opsForValue().set(this.genShiroSessionId(sessionId), customSession, customSession.getTimeout(), TimeUnit.MILLISECONDS);
        if (isCreate) {
            log.info(String.format("doCreate >>>>> sessionId=%s", sessionId));
        } else {
            log.info(String.format("doUpdate >>>>> sessionId=%s", sessionId));
        }
        log.info(String.format("status >>>>> onlineStatus=%s", customSession.getOnlineStatusEnum()));
    }

    @Override
    public void deleteShiroSession(Serializable sessionId) {
        redisTemplate.delete(this.genShiroSessionId(sessionId));
        log.info(String.format("doDelete >>>>> sessionId=%s", sessionId));
    }

    @Override
    public void deleteClientSessionId(String sessionId) {
        // 局部会话code
        String code = (String) redisTemplate.opsForValue().get(this.genClientSessionId(sessionId));
        // 删除局部会话
        redisTemplate.delete(code);
        // 删除同一个code（server）注册的局部会话
        redisTemplate.opsForSet().remove(this.genClientSessionIds(code), sessionId);
    }

    @Override
    public void deleteServerSessionId(String sessionId) {
        // 全局会话code
        String code = (String) redisTemplate.opsForValue().get(this.genServerSessionId(sessionId));
        // 清除全局会话
        redisTemplate.delete(code);
        // 清除code校验值
        redisTemplate.delete(this.genServerCode(code));

            /*
             清除该server下所有局部会话
             */

        // server下的client会话【们】
        String clientSessionIdsCode = this.genClientSessionIds(code);
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

        this.deleteSessionFromServerSessionIds(sessionId);
    }

    @Override
    public void deleteSessionFromServerSessionIds(String sessionId) {
        // 维护会话id列表，就是把它删掉
        redisTemplate.opsForSet().remove(this.genServerSessionIds(), sessionId);
    }

    @Override
    public long getServerCount() {
        return redisTemplate.opsForList().size(this.genServerSessionIds());
    }

    @Override
    public List<Object> getServerSession(int offset, int limit) {
        return redisTemplate.opsForList().range(this.genServerSessionIds(), offset, limit);
    }
}
