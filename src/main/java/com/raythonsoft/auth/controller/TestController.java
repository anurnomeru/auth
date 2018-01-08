package com.raythonsoft.auth.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.raythonsoft.auth.common.Result;
import com.raythonsoft.auth.common.ResultGenerator;
import com.raythonsoft.auth.model.Permission;
import com.raythonsoft.auth.model.RolePermission;
import com.raythonsoft.auth.model.Subject;
import com.raythonsoft.auth.service.PermissionService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Anur IjuoKaruKas on 2017/12/13.
 * Description :
 */
@Log4j
@RestController
public class TestController {

    @Autowired
    PermissionService permissionService;

    @GetMapping("/test")
    public Result test(Subject subject) {
        PageInfo<Subject> o = permissionService.findPageBySubject(subject);
        log.info(JSON.toJSONString(o));
        List<Subject> permissionList = o.getList();
        for (Iterator<Subject> iterator = permissionList.iterator(); iterator.hasNext(); ) {
            Subject next = iterator.next();
            log.info(next.toString());
        }
        return ResultGenerator.genSuccessResult(o);
    }
}
