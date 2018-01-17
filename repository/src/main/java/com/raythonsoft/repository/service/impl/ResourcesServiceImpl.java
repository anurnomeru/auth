package com.raythonsoft.repository.service.impl;


import com.raythonsoft.repository.core.AbstractService;
import com.raythonsoft.repository.dao.ResourcesMapper;
import com.raythonsoft.repository.model.Resources;
import com.raythonsoft.repository.service.ResourcesService;
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
