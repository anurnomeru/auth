package com.raythonsoft.sso.service.impl;


import com.raythonsoft.sso.dao.RoleResourcesMapper;
import com.raythonsoft.sso.core.AbstractService;
import com.raythonsoft.sso.model.RoleResources;
import com.raythonsoft.sso.service.RoleResourcesService;
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
