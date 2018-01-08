package com.raythonsoft.auth.service.impl;

import com.raythonsoft.auth.dao.ProjectMapper;
import com.raythonsoft.auth.model.Project;
import com.raythonsoft.auth.service.ProjectService;
import com.raythonsoft.auth.core.AbstractService;
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
