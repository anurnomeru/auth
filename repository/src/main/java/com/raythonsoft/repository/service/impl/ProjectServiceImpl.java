package com.raythonsoft.repository.service.impl;

import com.raythonsoft.repository.core.AbstractService;
import com.raythonsoft.repository.dao.ProjectMapper;
import com.raythonsoft.repository.model.Project;
import com.raythonsoft.repository.service.ProjectService;
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
