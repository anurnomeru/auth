package com.raythonsoft.repository.service.impl;

import com.raythonsoft.repository.dao.LogMapper;
import com.raythonsoft.repository.model.Log;
import com.raythonsoft.repository.service.LogService;
import com.raythonsoft.repository.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by RAYTHONSOFT on 2018/01/26.
 */
@Service
@Transactional
public class LogServiceImpl extends AbstractService<Log> implements LogService {
    @Resource
    private LogMapper logMapper;

}
