package com.raythonsoft.sso.service.impl;

import com.raythonsoft.sso.dao.LogMapper;
import com.raythonsoft.sso.model.Log;
import com.raythonsoft.sso.service.LogService;
import com.raythonsoft.sso.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by RAYTHONSOFT on 2018/01/30.
 */
@Service
@Transactional
public class LogServiceImpl extends AbstractService<Log> implements LogService {
    @Resource
    private LogMapper logMapper;

}
