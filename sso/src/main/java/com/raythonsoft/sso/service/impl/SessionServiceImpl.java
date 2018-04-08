package com.raythonsoft.sso.service.impl;

import com.raythonsoft.common.util.StringUtils;
import com.raythonsoft.sso.repository.CodeRedisRepository;
import com.raythonsoft.sso.repository.SessionIdGenerator;
import com.raythonsoft.sso.repository.SessionOperationRepository;
import com.raythonsoft.sso.service.AuthSystemService;
import com.raythonsoft.sso.service.SessionService;
import com.raythonsoft.sso.session.OnlineStatusEnum;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by Anur IjuoKaruKas on 2018/4/8.
 * Description :
 */
@Service
public class SessionServiceImpl implements SessionService {

    private final CodeRedisRepository codeRedisRepository;
    private final SessionIdGenerator sessionIdGenerator;
    private final SessionOperationRepository sessionOperationRepository;
    private final AuthSystemService authSystemService;

    @Autowired
    public SessionServiceImpl(CodeRedisRepository codeRedisRepository, SessionIdGenerator sessionIdGenerator, SessionOperationRepository sessionOperationRepository, AuthSystemService authSystemService) {
        this.codeRedisRepository = codeRedisRepository;
        this.sessionIdGenerator = sessionIdGenerator;
        this.sessionOperationRepository = sessionOperationRepository;
        this.authSystemService = authSystemService;
    }

    @Override
    public boolean isEffective(Session session) {
        return !StringUtils.isEmpty(codeRedisRepository.getCheckCodeByGenningSessionId(sessionIdGenerator.genServerSessionId(session.getId())));
    }

    @Override
    public String sessionEffective(Session session) {
        String serverSessionId = String.valueOf(session.getId());

        // 更改状态为：在线
        sessionOperationRepository.updateShiroSessionStatus(serverSessionId, OnlineStatusEnum.ON_LINE);
        // 将这个sessionId 存入全局会话
        sessionOperationRepository.leftPushIntoServerSessionIds(serverSessionId);

        // 全局会话code
        String checkCode = String.valueOf(UUID.randomUUID());

        // 维护全局会话的checkCode
        codeRedisRepository.setCheckCodeIntoGenningSessionId(sessionIdGenerator.genServerSessionId(serverSessionId),
                checkCode,
                (int) session.getTimeout() / 1000,
                TimeUnit.SECONDS);

        // 初始化 code 的校验值
        codeRedisRepository.setCheckCode(checkCode, (int) session.getTimeout() / 1000, TimeUnit.SECONDS);

        return checkCode;
    }
}
