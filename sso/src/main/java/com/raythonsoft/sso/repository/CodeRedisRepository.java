package com.raythonsoft.sso.repository;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Created by Anur IjuoKaruKas on 2018/1/26.
 * Description :
 * CheckCode ： 原始code
 * CacheCode ： 维护会话列表的code（由checkCode加上标识）
 */
public interface CodeRedisRepository {

    /**
     * 根据会话，获取checkCode
     *
     * @param genningSessionId
     * @return
     */
    String getCheckCodeByGenningSessionId(String genningSessionId);

    /**
     * 创建会话，将checkCode保存在sessionId下
     *
     * @param genningSessionId
     * @param checkCode
     * @param timeout
     * @param timeUnit
     */
    void setCheckCodeIntoGenningSessionId(String genningSessionId, String checkCode, Integer timeout, TimeUnit timeUnit);

    /**
     * 初始化checkCode
     *
     * @param checkCode
     * @param timeout
     * @param timeUnit
     */
    void setCheckCode(String checkCode, Integer timeout, TimeUnit timeUnit);

    /**
     * 删除checkCode
     *
     * @param checkCode
     */
    void removeCheckCode(String checkCode);

    /**
     * 刷新该SessionIds <Set> 的超时时间
     * key ： code
     * value ：sessionId（局部）
     *
     * @param code
     * @param timeout
     * @param timeUnit
     */
    void expireCacheCode(String code, Integer timeout, TimeUnit timeUnit);

    /**
     * 向该SessionIds的 <Set> 下添加元素，并更新超时时间
     *
     * @param code
     * @param sessionId
     * @param timeout
     * @param timeUnit
     */
    void saddCacheCode(String code, Serializable sessionId, Integer timeout, TimeUnit timeUnit);

    /**
     * 查询code下剩余注册个数
     *
     * @param code
     */
    void scardCacheCode(String code);
}
