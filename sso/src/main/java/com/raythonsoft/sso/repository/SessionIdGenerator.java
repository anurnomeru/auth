package com.raythonsoft.sso.repository;

import java.io.Serializable;

/**
 * Created by Anur IjuoKaruKas on 2018/1/26.
 * Description : 可装配各种SessionId的RedisKey
 */
public interface SessionIdGenerator {

    /**
     * 生成shiroSessionId
     *
     * @param sessionId
     * @return
     */
    String genShiroSessionId(Serializable sessionId);

    /**
     * 生成clientSessionId
     *
     * @param sessionId
     * @return
     */
    String genClientSessionId(Serializable sessionId);

    /**
     * 生成clientSessionIds
     *
     * @param code
     * @return
     */
    String genClientSessionIdsCodeParamCode(String code);

    /**
     * 生成serverSessionId
     *
     * @param sessionId
     * @return
     */
    String genServerSessionId(Serializable sessionId);

    /**
     * 生成serverSessionIds
     *
     * @return
     */
    String genServerSessionIds();

    /**
     * 生成server校验值
     *
     * @param code
     * @return
     */
    String genCheckCode(String code);
}
