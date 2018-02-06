package com.raythonsoft.sso.service.impl;

import com.raythonsoft.sso.dao.AuthRolePermissionMapper;
import com.raythonsoft.sso.model.AuthRolePermission;
import com.raythonsoft.sso.service.AuthRolePermissionService;
import com.raythonsoft.sso.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by RAYTHONSOFT on 2018/02/06.
 */
@Service
@Transactional
public class AuthRolePermissionServiceImpl extends AbstractService<AuthRolePermission> implements AuthRolePermissionService {
    @Resource
    private AuthRolePermissionMapper authRolePermissionMapper;

}
