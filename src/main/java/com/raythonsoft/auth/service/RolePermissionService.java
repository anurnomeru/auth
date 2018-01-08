package com.raythonsoft.auth.service;

import com.github.pagehelper.PageInfo;
import com.raythonsoft.auth.model.RolePermission;
import com.raythonsoft.auth.core.Service;


/**
 * Created by RAYTHONSOFT on 2017/12/13.
 */
public interface RolePermissionService extends Service<RolePermission> {

    /**
     * 条件与分页获取
     *
     * @param rolePermission
     * @return
     */
    PageInfo<RolePermission> findPageBy(RolePermission rolePermission);
}
