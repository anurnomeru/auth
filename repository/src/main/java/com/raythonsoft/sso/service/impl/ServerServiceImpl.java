package com.raythonsoft.sso.service.impl;


import com.raythonsoft.sso.core.AbstractService;
import com.raythonsoft.sso.dao.ServerMapper;
import com.raythonsoft.sso.model.Server;
import com.raythonsoft.sso.service.ServerService;
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
