package com.raythonsoft.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Anur IjuoKaruKas on 2018/4/8.
 * Description :
 */
@RestController
public class TestController {

    @GetMapping("test")
    public String test(String checkCode) {
        return checkCode;
    }
}
