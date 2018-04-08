package com.raythonsoft.sso.controller;

import com.raythonsoft.sso.repository.CodeRedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


/**
 * Created by Anur IjuoKaruKas on 2018/4/8.
 * Description :
 */
@Controller
public class TestController {

    @Autowired
    private CodeRedisRepository codeRedisRepository;

    public void test() {
        codeRedisRepository.removeCheckCode("1");
    }
}
