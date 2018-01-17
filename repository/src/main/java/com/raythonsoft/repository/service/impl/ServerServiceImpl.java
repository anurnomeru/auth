package com.raythonsoft.repository.service.impl;


import com.raythonsoft.repository.core.AbstractService;
import com.raythonsoft.repository.dao.ServerMapper;
import com.raythonsoft.repository.model.Server;
import com.raythonsoft.repository.service.ServerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by RAYTHONSOFT on 2018/01/08.
 */
@Service
@Transactional
public class ServerServiceImpl extends AbstractService<Server> implements ServerService {
    @Resource
    private ServerMapper serverMapper;

}
