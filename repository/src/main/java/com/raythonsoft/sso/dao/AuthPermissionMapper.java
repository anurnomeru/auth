package com.raythonsoft.sso.dao;

import com.raythonsoft.sso.core.Mapper;
import com.raythonsoft.sso.model.AuthPermission;

import java.util.List;

public interface AuthPermissionMapper extends Mapper<AuthPermission> {

    /**
     * 根据 userId 查找权限
     *
     * @param userId
     * @return
     */
    List<AuthPermission> selectByUserId(Integer userId);
}