package com.raythonsoft.auth.dao;

import com.raythonsoft.auth.core.Mapper;
import com.raythonsoft.auth.model.Permission;
import com.raythonsoft.auth.model.RolePermission;
import com.raythonsoft.auth.model.Subject;

import java.util.List;

public interface PermissionMapper extends Mapper<Permission>{

    /**
     * 根据角色id获取
     *
     * @param roleIdList
     * @return
     */
    List<RolePermission> selectByRoleIdList(List<Integer> roleIdList);

    /**
     * 根据subject获取
     *
     * @param subject
     * @return
     */
    List<Subject> selectBySubject(Subject subject);
}