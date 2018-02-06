package com.raythonsoft.sso.service.impl;

import com.raythonsoft.sso.dao.AuthUserPermissionMapper;
import com.raythonsoft.sso.model.AuthUserPermission;
import com.raythonsoft.sso.service.AuthUserPermissionService;
import com.raythonsoft.sso.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by RAYTHONSOFT on 2018/02/06.
 */
@Service
@Transactional
public class AuthUserPermissionServiceImpl extends AbstractService<AuthUserPermission> implements AuthUserPermissionService {
    @Resource
    private AuthUserPermissionMapper authUserPermissionMapper;

}
