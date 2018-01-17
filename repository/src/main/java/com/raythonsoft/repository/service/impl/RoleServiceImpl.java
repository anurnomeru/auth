package com.raythonsoft.repository.service.impl;


import com.raythonsoft.repository.core.AbstractService;
import com.raythonsoft.repository.dao.RoleMapper;
import com.raythonsoft.repository.model.Role;
import com.raythonsoft.repository.service.RoleService;
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
