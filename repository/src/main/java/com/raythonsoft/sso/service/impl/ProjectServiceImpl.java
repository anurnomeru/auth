package com.raythonsoft.sso.service.impl;

import com.raythonsoft.sso.core.AbstractService;
import com.raythonsoft.sso.dao.ProjectMapper;
import com.raythonsoft.sso.model.Project;
import com.raythonsoft.sso.service.ProjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by RAYTHONSOFT on 2018/01/08.
 */
@Service
@Transactional
public class ProjectServiceImpl extends AbstractService<Project> implements ProjectService {
    @Resource
    private ProjectMapper projectMapper;

}