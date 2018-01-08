package com.raythonsoft.auth.service;

import com.github.pagehelper.PageInfo;
import com.raythonsoft.auth.model.Subject;
import com.raythonsoft.auth.core.Service;


/**
 * Created by RAYTHONSOFT on 2017/12/13.
 */
public interface SubjectService extends Service<Subject> {

    /**
     * 条件与分页获取
     *
     * @param subject
     * @return
     */
    PageInfo<Subject> findPageBy(Subject subject);
}
