package com.raythonsoft.auth.service;

import com.github.pagehelper.PageInfo;
import com.raythonsoft.auth.model.Permission;
import com.raythonsoft.auth.core.Service;
import com.raythonsoft.auth.model.RolePermission;
import com.raythonsoft.auth.model.Subject;

import java.util.List;

/**
 * Created by RAYTHONSOFT on 2017/12/13.
 */
public interface PermissionService extends Service<Permission> {

    /**
     * 条件与分页获取
     *
     * @param permission
     * @return
     */
    PageInfo<Permission> findPageBy(Permission permission);

    /**
     * 根据角色获取
     *
     * @param roleIdList
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<RolePermission> findPageByRoleIdList(List<Integer> roleIdList, Integer pageNum, Integer pageSize);

    /**
     * 根据subject获取
     *
     * @param subject
     * @return
     */
    PageInfo<Subject> findPageBySubject(Subject subject);
}
