package com.raythonsoft.auth.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.raythonsoft.auth.dao.RoleMapper;
import com.raythonsoft.auth.model.Role;
import com.raythonsoft.auth.model.SubjectRole;
import com.raythonsoft.auth.service.RoleService;
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
public class RoleServiceImpl extends AbstractService<Role> implements RoleService {
    @Resource
    private RoleMapper roleMapper;

    @Override
    public PageInfo<Role> findPageBy(Role role) {
        Example example = new Condition(Role.class);
        Example.Criteria criteria = example.createCriteria();

        if (null != role.getName()) {
            criteria.andEqualTo("name", role.getName());
        }

        PageHelper.startPage(role.getPageNum(), role.getPageSize());
        List<Role> roleList = roleMapper.selectByCondition(example);
        return new PageInfo<>(roleList);
    }
}
