package com.raythonsoft.sso.service.impl;

import com.raythonsoft.sso.dao.AuthPermissionMapper;
import com.raythonsoft.sso.model.AuthPermission;
import com.raythonsoft.sso.service.AuthPermissionService;
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
public class AuthPermissionServiceImpl extends AbstractService<AuthPermission> implements AuthPermissionService {
    @Resource
    private AuthPermissionMapper authPermissionMapper;

    @Override
    public List<AuthPermission> findByUserId(Integer userId) {
        return authPermissionMapper.selectByUserId(userId);
    }
}
