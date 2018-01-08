package com.raythonsoft.auth.service;

import com.github.pagehelper.PageInfo;
import com.raythonsoft.auth.model.SubjectRole;
import com.raythonsoft.auth.core.Service;


/**
 * Created by RAYTHONSOFT on 2017/12/13.
 */
public interface SubjectRoleService extends Service<SubjectRole> {

    /**
     * 条件与分页获取
     *
     * @param subjectRole
     * @return
     */
    PageInfo<SubjectRole> findPageBy(SubjectRole subjectRole);
}
