package com.raythonsoft.auth.controller;


import com.raythonsoft.auth.model.Resources;
import com.raythonsoft.auth.service.ResourcesService;
import lombok.extern.log4j.Log4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.HostAuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * Created by Anur IjuoKaruKas on 2017/12/13.
 * Description :
 */
@Log4j
@RestController
public class TestController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ResourcesService resourcesService;

    @GetMapping("/test")
    public List<Resources> test() {
        List<Resources> resourcesList
                = redisTemplate.opsForList().range("test", 0, -1);
        resourcesList = resourcesService.findAllByUserId(1);
        redisTemplate.opsForList().leftPushAll("test", resourcesList);

        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("1", "1");
        SecurityUtils.getSubject().login(usernamePasswordToken);
        return resourcesList;
    }
}
