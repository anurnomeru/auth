package com.raythonsoft.sso.service.impl;

import com.raythonsoft.sso.dao.AuthSystemMapper;
import com.raythonsoft.sso.model.AuthSystem;
import com.raythonsoft.sso.service.AuthSystemService;
import com.raythonsoft.sso.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by RAYTHONSOFT on 2018/02/06.
 */
@Service
@Transactional
public class AuthSystemServiceImpl extends AbstractService<AuthSystem> implements AuthSystemService {
    @Resource
    private AuthSystemMapper authSystemMapper;

}
