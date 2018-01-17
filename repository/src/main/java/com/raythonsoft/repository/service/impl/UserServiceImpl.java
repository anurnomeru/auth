package com.raythonsoft.repository.service.impl;


import com.raythonsoft.repository.core.AbstractService;
import com.raythonsoft.repository.dao.UserMapper;
import com.raythonsoft.repository.model.User;
import com.raythonsoft.repository.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by RAYTHONSOFT on 2018/01/08.
 */
@Service
@Transactional
public class UserServiceImpl extends AbstractService<User> implements UserService {
    @Resource
    private UserMapper userMapper;

}
