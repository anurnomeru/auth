package com.raythonsoft.sso.service;

import com.raythonsoft.sso.model.AuthPermission;
import com.raythonsoft.sso.core.Service;

import java.util.List;


/**
 * Created by RAYTHONSOFT on 2018/02/06.
 */
public interface AuthPermissionService extends Service<AuthPermission> {

    /**
     * 根据 userId 查找响应权限
     *
     * @param userId
     * @return
     */
    List<AuthPermission> findByUserId(Integer userId);
}
