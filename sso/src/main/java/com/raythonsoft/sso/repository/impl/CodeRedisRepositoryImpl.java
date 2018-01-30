package com.raythonsoft.sso.repository.impl;

import com.raythonsoft.sso.repository.CodeRedisRepository;
import com.raythonsoft.sso.repository.SessionIdGenerator;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Created by Anur IjuoKaruKas on 2018/1/26.
 * Description :
 */
@Repository
@Log4j
public class CodeRedisRepositoryImpl implements CodeRedisRepository {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private SessionIdGenerator sessionIdGenerator;

    @Override
    public String getCheckCodeByGenningSessionId(String genningSessionId) {
        return String.valueOf(redisTemplate.opsForValue().get(genningSessionId));
    }

    @Override
    public void setCheckCodeIntoGenningSessionId(String genningSessionId, String checkCode, Integer timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(genningSessionId, checkCode, timeout, timeUnit);
    }

    @Override
    public void setCheckCode(String checkCode, Integer timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(sessionIdGenerator.genServerCode(checkCode), checkCode, timeout, timeUnit);
    }

    @Override
    public void removeCheckCode(String checkCode) {
        redisTemplate.delete(sessionIdGenerator.genServerCode(checkCode));
    }

    @Override
    public void expireCacheCode(String code, Integer timeout, TimeUnit timeUnit) {
        redisTemplate.expire(code, timeout, timeUnit);
    }

    @Override
    public void saddCacheCode(String code, Serializable sessionId, Integer timeout, TimeUnit timeUnit) {
        redisTemplate.opsForSet().add(code, sessionId);
        this.expireCacheCode(code, timeout, timeUnit);
    }

    @Override
    public void scardCacheCode(String code) {
        log.info(String.format("当前code=%s，对应的注册系统个数：%s个",
                code,
                redisTemplate.opsForSet().size(code)
                )
        );
    }
}
