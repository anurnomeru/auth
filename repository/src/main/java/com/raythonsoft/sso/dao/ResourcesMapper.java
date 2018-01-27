package com.raythonsoft.sso.dao;


import com.raythonsoft.sso.core.Mapper;
import com.raythonsoft.sso.model.Resources;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourcesMapper extends Mapper<Resources> {
    List<Resources> selectAllByUserId(Integer userId);
}