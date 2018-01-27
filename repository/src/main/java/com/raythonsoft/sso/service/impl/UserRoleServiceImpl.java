package com.raythonsoft.sso.service.impl;


import com.raythonsoft.sso.dao.UserRoleMapper;
import com.raythonsoft.sso.core.AbstractService;
import com.raythonsoft.sso.model.UserRole;
import com.raythonsoft.sso.service.UserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by RAYTHONSOFT on 2018/01/08.
 */
@Service
@Transactional
public class UserRoleServiceImpl extends AbstractService<UserRole> implements UserRoleService {
    @Resource
    private UserRoleMapper userRoleMapper;

}
