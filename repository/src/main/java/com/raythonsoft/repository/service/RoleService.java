package com.raythonsoft.repository.service;


import com.raythonsoft.repository.core.Service;
import com.raythonsoft.repository.model.Role;

import java.util.List;

/**
 * Created by RAYTHONSOFT on 2018/01/08.
 */
public interface RoleService extends Service<Role> {

    /**
     * 根据用户id查询所有角色
     *
     * @param userId
     * @return
     */
    List<Role> findAllByUserId(Integer userId);
}
