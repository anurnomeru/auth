package com.raythonsoft.auth.service.impl;

import com.raythonsoft.auth.dao.SystemMapper;
import com.raythonsoft.auth.model.System;
import com.raythonsoft.auth.service.SystemService;
import com.raythonsoft.auth.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by RAYTHONSOFT on 2018/01/08.
 */
@Service
@Transactional
public class SystemServiceImpl extends AbstractService<System> implements SystemService {
    @Resource
    private SystemMapper systemMapper;

}
