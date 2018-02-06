package com.raythonsoft.sso.dao;

import com.raythonsoft.sso.core.Mapper;
import com.raythonsoft.sso.model.AuthRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AuthRoleMapper extends Mapper<AuthRole> {

    /**
     * 根据 userId 查找角色
     *
     * @param userId
     * @return
     */
    List<AuthRole> selectByUserId(@Param(value = "userId") Integer userId);
}