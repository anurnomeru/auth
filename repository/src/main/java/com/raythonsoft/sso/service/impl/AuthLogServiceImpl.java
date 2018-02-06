package com.raythonsoft.sso.service.impl;

import com.raythonsoft.sso.dao.AuthLogMapper;
import com.raythonsoft.sso.model.AuthLog;
import com.raythonsoft.sso.service.AuthLogService;
import com.raythonsoft.sso.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by RAYTHONSOFT on 2018/02/06.
 */
@Service
@Transactional
public class AuthLogServiceImpl extends AbstractService<AuthLog> implements AuthLogService {
    @Resource
    private AuthLogMapper authLogMapper;

}
