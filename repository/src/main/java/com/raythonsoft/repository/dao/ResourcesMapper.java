package com.raythonsoft.repository.dao;


import com.raythonsoft.repository.core.Mapper;
import com.raythonsoft.repository.model.Resources;

import java.util.List;

public interface ResourcesMapper extends Mapper<Resources> {
    List<Resources> selectAllByUserId(Integer userId);
}