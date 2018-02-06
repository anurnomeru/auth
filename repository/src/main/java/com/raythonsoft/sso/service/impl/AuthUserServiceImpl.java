package com.raythonsoft.sso.service.impl;

import com.raythonsoft.sso.dao.AuthUserMapper;
import com.raythonsoft.sso.model.AuthUser;
import com.raythonsoft.sso.service.AuthUserService;
import com.raythonsoft.sso.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by RAYTHONSOFT on 2018/02/06.
 */
@Service
@Transactional
public class AuthUserServiceImpl extends AbstractService<AuthUser> implements AuthUserService {
    @Resource
    private AuthUserMapper authUserMapper;

}
