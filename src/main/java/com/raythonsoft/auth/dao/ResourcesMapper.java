package com.raythonsoft.auth.dao;

import com.raythonsoft.auth.core.Mapper;
import com.raythonsoft.auth.model.Resources;
import com.raythonsoft.auth.model.Role;

import java.util.List;

public interface ResourcesMapper extends Mapper<Resources> {
    List<Resources> selectAllByUserId(Integer userId);
}