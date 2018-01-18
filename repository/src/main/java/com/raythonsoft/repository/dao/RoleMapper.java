package com.raythonsoft.repository.dao;


import com.raythonsoft.repository.core.Mapper;
import com.raythonsoft.repository.model.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMapper extends Mapper<Role> {
    List<Role> selectAllByUserId(Integer userId);
}