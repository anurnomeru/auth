package com.raythonsoft.sso.service;
import com.raythonsoft.sso.model.AuthRole;
import com.raythonsoft.sso.core.Service;

import java.util.List;


/**
 * Created by RAYTHONSOFT on 2018/02/06.
 */
public interface AuthRoleService extends Service<AuthRole> {

    /**
     * 根据 userId 查找角色
     * @param userId
     * @return
     */
    List<AuthRole> findByUserId(Integer userId);
}
