package com.raythonsoft.sso.service.impl;

import com.raythonsoft.sso.dao.SsoLogMapper;
import com.raythonsoft.sso.model.SsoLog;
import com.raythonsoft.sso.service.SsoLogService;
import com.raythonsoft.sso.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by RAYTHONSOFT on 2018/01/27.
 */
@Service
@Transactional
public class SsoLogServiceImpl extends AbstractService<SsoLog> implements SsoLogService {
    @Resource
    private SsoLogMapper ssoLogMapper;

}
