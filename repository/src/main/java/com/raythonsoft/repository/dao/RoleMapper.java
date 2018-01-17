package com.raythonsoft.repository.dao;


import com.raythonsoft.repository.core.Mapper;
import com.raythonsoft.repository.model.Role;

import java.util.List;

public interface RoleMapper extends Mapper<Role> {
    List<Role> selectAllByUserId(Integer userId);
}