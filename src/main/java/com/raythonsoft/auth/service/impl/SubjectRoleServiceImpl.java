package com.raythonsoft.auth.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.raythonsoft.auth.dao.SubjectRoleMapper;
import com.raythonsoft.auth.model.SubjectRole;
import com.raythonsoft.auth.service.SubjectRoleService;
import com.raythonsoft.auth.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by RAYTHONSOFT on 2017/12/13.
 */
@Service
@Transactional
public class SubjectRoleServiceImpl extends AbstractService<SubjectRole> implements SubjectRoleService {
    @Resource
    private SubjectRoleMapper subjectRoleMapper;

    @Override
    public PageInfo<SubjectRole> findPageBy(SubjectRole subjectRole) {
        Example example = new Condition(SubjectRole.class);
        Example.Criteria criteria = example.createCriteria();

        if (null != subjectRole.getRoleId()) {
            criteria.andEqualTo("roleId", subjectRole.getRoleId());
        }

        if (null != subjectRole.getRoleId()) {
            criteria.andEqualTo("subjectId", subjectRole.getSubjectId());
        }

        PageHelper.startPage(subjectRole.getPageNum(), subjectRole.getPageSize());
        List<SubjectRole> subjectRoleList = subjectRoleMapper.selectByCondition(example);
        return new PageInfo<>(subjectRoleList);
    }
}
