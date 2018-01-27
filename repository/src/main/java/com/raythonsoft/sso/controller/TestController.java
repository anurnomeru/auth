package com.raythonsoft.sso.controller;

import com.raythonsoft.common.model.Result;
import com.raythonsoft.common.util.ResultGenerator;
import com.raythonsoft.sso.model.SsoLog;
import com.raythonsoft.sso.service.ResourcesService;
import com.raythonsoft.sso.service.SsoLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Anur IjuoKaruKas on 2018/1/27.
 * Description :
 */
@RestController
public class TestController {

    @Autowired
    private ResourcesService resourcesService;

    @Autowired
    private SsoLogService ssoLogService;

    @GetMapping("/test")
    public Result test(Integer userId) {
        ssoLogService.save(SsoLog.builder().build());
        return ResultGenerator.genSuccessResult(resourcesService.findAllByUserId(userId));
    }
}
