package com.raythonsoft.repository.service.impl;


import com.raythonsoft.repository.core.AbstractService;
import com.raythonsoft.repository.dao.RoleResourcesMapper;
import com.raythonsoft.repository.model.RoleResources;
import com.raythonsoft.repository.service.RoleResourcesService;
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
