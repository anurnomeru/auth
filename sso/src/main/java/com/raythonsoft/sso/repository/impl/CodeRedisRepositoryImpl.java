package com.raythonsoft.sso.repository.impl;

import com.raythonsoft.sso.properties.ShiroProperties;
import com.raythonsoft.sso.repository.CodeRedisRepository;
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

    @Override
    public String getCodeByGenningSessionId(String genningSessionId) {
        return String.valueOf(redisTemplate.opsForValue().get(genningSessionId));
    }

    @Override
    public void setCodeByGenningSessionId(String genningSessionId, String code, Integer timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(genningSessionId, code, timeout, timeUnit);
    }

    @Override
    public void expireCode(String code, Integer timeout, TimeUnit timeUnit) {
        redisTemplate.expire(code, timeout, timeUnit);
    }

    @Override
    public void saddCode(String code, Serializable sessionId, Integer timeout, TimeUnit timeUnit) {
        redisTemplate.opsForSet().add(code, sessionId);
        this.expireCode(code, timeout, timeUnit);
    }

    @Override
    public void scardCode(String code){
        log.info(String.format("当前code=%s，对应的注册系统个数：%s个",
                code,
                redisTemplate.opsForSet().size(code)
                )
        );
    }
}
