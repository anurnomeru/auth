package com.raythonsoft.auth.service;

import com.github.pagehelper.PageInfo;
import com.raythonsoft.auth.model.Role;
import com.raythonsoft.auth.core.Service;


/**
 * Created by RAYTHONSOFT on 2017/12/13.
 */
public interface RoleService extends Service<Role> {

    /**
     * 条件与分页获取
     *
     * @param role
     * @return
     */
    PageInfo<Role> findPageBy(Role role);
}
