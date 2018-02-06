package com.raythonsoft.sso.service.impl;

import com.raythonsoft.sso.dao.AuthUserOrganizationMapper;
import com.raythonsoft.sso.model.AuthUserOrganization;
import com.raythonsoft.sso.service.AuthUserOrganizationService;
import com.raythonsoft.sso.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by RAYTHONSOFT on 2018/02/06.
 */
@Service
@Transactional
public class AuthUserOrganizationServiceImpl extends AbstractService<AuthUserOrganization> implements AuthUserOrganizationService {
    @Resource
    private AuthUserOrganizationMapper authUserOrganizationMapper;

}
