package com.raythonsoft.sso.service;


import com.raythonsoft.sso.core.Service;
import com.raythonsoft.sso.model.Resources;

import java.util.List;

/**
 * Created by RAYTHONSOFT on 2018/01/08.
 */
public interface ResourcesService extends Service<Resources> {

    /**
     * 根据用户id查询所有权限
     *
     * @param userId
     * @return
     */
    List<Resources> findAllByUserId(Integer userId);
}
