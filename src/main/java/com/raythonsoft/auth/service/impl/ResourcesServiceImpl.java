package com.raythonsoft.auth.service.impl;

import com.raythonsoft.auth.dao.ResourcesMapper;
import com.raythonsoft.auth.model.Resources;
import com.raythonsoft.auth.model.Role;
import com.raythonsoft.auth.service.ResourcesService;
import com.raythonsoft.auth.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by RAYTHONSOFT on 2018/01/08.
 */
@Service
@Transactional
public class ResourcesServiceImpl extends AbstractService<Resources> implements ResourcesService {
    @Resource
    private ResourcesMapper resourcesMapper;

    @Override
    public List<Resources> findAllByUserId(Integer userId) {
        return resourcesMapper.selectAllByUserId(userId);
    }
}
