package com.raythonsoft.sso.service.impl;

import com.raythonsoft.sso.dao.AuthOrganizationMapper;
import com.raythonsoft.sso.model.AuthOrganization;
import com.raythonsoft.sso.service.AuthOrganizationService;
import com.raythonsoft.sso.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by RAYTHONSOFT on 2018/02/06.
 */
@Service
@Transactional
public class AuthOrganizationServiceImpl extends AbstractService<AuthOrganization> implements AuthOrganizationService {
    @Resource
    private AuthOrganizationMapper authOrganizationMapper;

}
