package com.raythonsoft.auth.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.raythonsoft.auth.dao.RolePermissionMapper;
import com.raythonsoft.auth.model.RolePermission;
import com.raythonsoft.auth.model.SubjectRole;
import com.raythonsoft.auth.service.RolePermissionService;
import com.raythonsoft.auth.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by RAYTHONSOFT on 2017/12/13.
 */
@Service
@Transactional
public class RolePermissionServiceImpl extends AbstractService<RolePermission> implements RolePermissionService {
    @Resource
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public PageInfo<RolePermission> findPageBy(RolePermission rolePermission) {
        Example example = new Condition(RolePermission.class);
        Example.Criteria criteria = example.createCriteria();

        if (null != rolePermission.getPermissionId()) {
            criteria.andEqualTo("permissionId", rolePermission.getPermissionId());
        }

        if (null != rolePermission.getRoleId()) {
            criteria.andEqualTo("roleId", rolePermission.getRoleId());
        }

        PageHelper.startPage(rolePermission.getPageNum(), rolePermission.getPageSize());
        List<RolePermission> rolePermissionList = rolePermissionMapper.selectByCondition(example);
        return new PageInfo<>(rolePermissionList);
    }
}
