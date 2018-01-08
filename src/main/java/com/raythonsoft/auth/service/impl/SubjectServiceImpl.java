package com.raythonsoft.auth.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.raythonsoft.auth.dao.SubjectMapper;
import com.raythonsoft.auth.model.Subject;
import com.raythonsoft.auth.service.SubjectService;
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
public class SubjectServiceImpl extends AbstractService<Subject> implements SubjectService {
    @Resource
    private SubjectMapper subjectMapper;

    @Override
    public PageInfo<Subject> findPageBy(Subject subject) {
        Example example = new Condition(Subject.class);
        Example.Criteria criteria = example.createCriteria();

        if (null != subject.getProject()) {
            criteria.andEqualTo("project", subject.getProject());
        }

        if (null != subject.getProject()) {
            criteria.andEqualTo("sign", subject.getSign());
        }

        PageHelper.startPage(subject.getPageNum(), subject.getPageSize());
        List<Subject> subjectList = subjectMapper.selectByCondition(example);
        return new PageInfo<>(subjectList);
    }
}
