package com.raythonsoft.auth.service.impl;

import com.raythonsoft.auth.dao.RoleMapper;
import com.raythonsoft.auth.model.Role;
import com.raythonsoft.auth.service.RoleService;
import com.raythonsoft.auth.core.AbstractService;
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
