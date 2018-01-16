package com.raythonsoft.auth.shiro.repository.impl;

import com.raythonsoft.auth.common.AuthConstant;
import com.raythonsoft.auth.shiro.properties.ShiroProperties;
import com.raythonsoft.auth.shiro.repository.SessionOperationRepository;
import lombok.extern.log4j.Log4j;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
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
    public void deleteClientSessionId(String sessionId) {
        // 局部会话code
        String code = (String) redisTemplate.opsForValue().get(String.format(AuthConstant.ID_FORM, shiroProperties.getAuthClientSessionId(), sessionId));
        // 删除局部会话
        redisTemplate.delete(code);
        // 删除同一个code（server）注册的局部会话
        redisTemplate.opsForSet().remove(String.format(AuthConstant.ID_FORM, shiroProperties.getAuthClientSessionIds(), code), sessionId);
    }

    @Override
    public void deleteServerSessionId(String sessionId) {
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

    @Override
    public void saveShiroSession(Session session, Serializable sessionId) {
        redisTemplate.opsForValue().set(this.genShiroSessionId(sessionId), session, session.getTimeout(), TimeUnit.MILLISECONDS);
    }

    @Override
    public Session getShiroSession(Serializable sessionId) {
        return (Session) redisTemplate.opsForValue().get(this.genShiroSessionId(sessionId));
    }

    @Override
    public void deleteShiroSession(Serializable sessionId) {
        redisTemplate.delete(this.genShiroSessionId(sessionId));
    }

    @Override
    public String genShiroSessionId(Serializable sessionId) {
        return String.format(AuthConstant.ID_FORM, shiroProperties.getAuthShiroSessionId(), sessionId);
    }
}
