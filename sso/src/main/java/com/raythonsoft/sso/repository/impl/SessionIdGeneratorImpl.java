package com.raythonsoft.sso.repository.impl;

import com.raythonsoft.sso.common.SsoConstant;
import com.raythonsoft.sso.properties.ShiroProperties;
import com.raythonsoft.sso.repository.SessionIdGenerator;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by Anur IjuoKaruKas on 2018/1/26.
 * Description :
 */
@Service
@Log4j
public class SessionIdGeneratorImpl implements SessionIdGenerator {

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
    public String genClientSessionIdsCodeParamCode(String code) {
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
    public String genCheckCode(String code) {
        return String.format(SsoConstant.ID_FORM, shiroProperties.getAuthCheckCode(), code);
    }
}
