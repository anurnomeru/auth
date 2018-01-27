package com.raythonsoft.sso.dao;


import com.raythonsoft.sso.core.Mapper;
import com.raythonsoft.sso.model.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMapper extends Mapper<Role> {
    List<Role> selectAllByUserId(Integer userId);
}