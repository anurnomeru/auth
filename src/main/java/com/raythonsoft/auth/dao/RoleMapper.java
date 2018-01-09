package com.raythonsoft.auth.dao;

import com.raythonsoft.auth.core.Mapper;
import com.raythonsoft.auth.model.Role;

import java.util.List;

public interface RoleMapper extends Mapper<Role> {
    List<Role> selectAllByUserId(Integer userId);
}