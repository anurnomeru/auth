package com.raythonsoft.sso.service.impl;

import com.raythonsoft.sso.dao.AuthUserRoleMapper;
import com.raythonsoft.sso.model.AuthUserRole;
import com.raythonsoft.sso.service.AuthUserRoleService;
import com.raythonsoft.sso.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by RAYTHONSOFT on 2018/02/06.
 */
@Service
@Transactional
public class AuthUserRoleServiceImpl extends AbstractService<AuthUserRole> implements AuthUserRoleService {
    @Resource
    private AuthUserRoleMapper authUserRoleMapper;

}
