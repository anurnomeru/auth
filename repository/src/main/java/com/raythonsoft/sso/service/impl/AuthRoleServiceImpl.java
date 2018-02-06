package com.raythonsoft.sso.service.impl;

import com.raythonsoft.sso.dao.AuthRoleMapper;
import com.raythonsoft.sso.model.AuthRole;
import com.raythonsoft.sso.service.AuthRoleService;
import com.raythonsoft.sso.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by RAYTHONSOFT on 2018/02/06.
 */
@Service
@Transactional
public class AuthRoleServiceImpl extends AbstractService<AuthRole> implements AuthRoleService {
    @Resource
    private AuthRoleMapper authRoleMapper;

    @Override
    public List<AuthRole> findByUserId(Integer userId) {
        return authRoleMapper.selectByUserId(userId);
    }
}
