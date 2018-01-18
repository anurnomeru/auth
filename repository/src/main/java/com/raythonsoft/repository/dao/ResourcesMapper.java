package com.raythonsoft.repository.dao;


import com.raythonsoft.repository.core.Mapper;
import com.raythonsoft.repository.model.Resources;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourcesMapper extends Mapper<Resources> {
    List<Resources> selectAllByUserId(Integer userId);
}