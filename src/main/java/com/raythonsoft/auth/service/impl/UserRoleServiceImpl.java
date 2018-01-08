package com.raythonsoft.auth.service.impl;

import com.raythonsoft.auth.dao.UserRoleMapper;
import com.raythonsoft.auth.model.UserRole;
import com.raythonsoft.auth.service.UserRoleService;
import com.raythonsoft.auth.core.AbstractService;
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
