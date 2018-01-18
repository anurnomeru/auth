package com.raythonsoft.repository.controller;

import com.alibaba.fastjson.JSON;
import com.raythonsoft.repository.service.ResourcesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Anur IjuoKaruKas on 2018/1/17.
 * Description :
 */
@RestController
public class TestController {

    @Autowired
    private ResourcesService resourcesService;

    @GetMapping("/test")
    public String test(){
        return JSON.toJSONString(resourcesService.findAllByUserId(1));
    }
}
