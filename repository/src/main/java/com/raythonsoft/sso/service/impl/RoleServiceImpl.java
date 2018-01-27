package com.raythonsoft.sso.service.impl;


import com.raythonsoft.sso.dao.RoleMapper;
import com.raythonsoft.sso.core.AbstractService;
import com.raythonsoft.sso.model.Role;
import com.raythonsoft.sso.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by RAYTHONSOFT on 2018/01/08.
 */
@Service
@Transactional
public class RoleServiceImpl extends AbstractService<Role> implements RoleService {
    @Resource
    private RoleMapper roleMapper;

    @Override
    public List<Role> findAllByUserId(Integer userId) {
        return roleMapper.selectAllByUserId(userId);
    }
}
