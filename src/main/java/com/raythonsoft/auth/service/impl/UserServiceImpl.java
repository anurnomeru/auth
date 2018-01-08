package com.raythonsoft.auth.service.impl;

import com.raythonsoft.auth.dao.UserMapper;
import com.raythonsoft.auth.model.User;
import com.raythonsoft.auth.service.UserService;
import com.raythonsoft.auth.core.AbstractService;
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
