package com.raythonsoft.auth.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.raythonsoft.auth.dao.PermissionMapper;
import com.raythonsoft.auth.model.Permission;
import com.raythonsoft.auth.model.RolePermission;
import com.raythonsoft.auth.model.Subject;
import com.raythonsoft.auth.service.PermissionService;
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
public class PermissionServiceImpl extends AbstractService<Permission> implements PermissionService {
    @Resource
    private PermissionMapper permissionMapper;

    @Override
    public PageInfo<Permission> findPageBy(Permission permission) {
        Example example = new Condition(Permission.class);
        Example.Criteria criteria = example.createCriteria();

        if (null != permission.getType()) {
            criteria.andEqualTo("type", permission.getType());
        }

        if (null != permission.getName()) {
            criteria.andEqualTo("name", permission.getName());
        }

        if (null != permission.getResource()) {
            criteria.andEqualTo("resource", permission.getResource());
        }

        if (null != permission.getParentId()) {
            criteria.andEqualTo("parentId", permission.getParentId());
        }

        PageHelper.startPage(permission.getPageNum(), permission.getPageSize());
        List<Permission> permissionList = permissionMapper.selectByCondition(example);
        return new PageInfo<>(permissionList);
    }

    public PageInfo<RolePermission> findPageByRoleIdList(List<Integer> roleIdList, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<RolePermission> permissionList = permissionMapper.selectByRoleIdList(roleIdList);
        return new PageInfo<>(permissionList);
    }

    public PageInfo<Subject> findPageBySubject(Subject subject) {
        PageHelper.startPage(subject.getPageNum(), subject.getPageSize());
        List<Subject> subjectList = permissionMapper.selectBySubject(subject);
        return new PageInfo<>(subjectList);
    }
}

