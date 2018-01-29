package com.raythonsoft.sso.repository;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Created by Anur IjuoKaruKas on 2018/1/26.
 * Description :
 */
public interface CodeRedisRepository {

    /**
     * 从redis获取cacheCode
     *
     * @param genningSessionId
     * @return
     */
    String getCodeByGenningSessionId(String genningSessionId);

    /**
     * 保存sessionId 到 code 中
     *
     * @param genningSessionId
     * @param code
     * @param timeout
     * @param timeUnit
     */
    void setCodeByGenningSessionId(String genningSessionId, String code, Integer timeout, TimeUnit timeUnit);

    /**
     * 初始化code
     *
     * @param genningCode
     * @param code
     * @param timeout
     * @param timeUnit
     */
    void setCode(String genningCode, String code, Integer timeout, TimeUnit timeUnit);

    /**
     * 更新cacheCode【超时时间（一般是这样）】（更新同一个code下所有局部会话key）
     *
     * @param code
     * @param timeout
     * @param timeUnit
     */
    void expireCode(String code, Integer timeout, TimeUnit timeUnit);

    /**
     * 向该SessionIds的 <Set> 下添加元素，并更新超时时间
     *
     * @param code
     * @param sessionId
     * @param timeout
     * @param timeUnit
     */
    void saddCode(String code, Serializable sessionId, Integer timeout, TimeUnit timeUnit);

    /**
     * 查询code下剩余注册个数
     *
     * @param code
     */
    void scardCode(String code);
}
