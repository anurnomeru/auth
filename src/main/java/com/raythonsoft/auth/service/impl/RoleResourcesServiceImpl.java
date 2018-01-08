package com.raythonsoft.auth.service.impl;

import com.raythonsoft.auth.dao.RoleResourcesMapper;
import com.raythonsoft.auth.model.RoleResources;
import com.raythonsoft.auth.service.RoleResourcesService;
import com.raythonsoft.auth.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by RAYTHONSOFT on 2018/01/08.
 */
@Service
@Transactional
public class RoleResourcesServiceImpl extends AbstractService<RoleResources> implements RoleResourcesService {
    @Resource
    private RoleResourcesMapper roleResourcesMapper;

}
