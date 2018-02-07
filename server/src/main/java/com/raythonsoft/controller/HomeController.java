package com.raythonsoft.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Anur IjuoKaruKas on 2018/2/7.
 * Description :
 */
@Controller
@RequestMapping
public class HomeController {

    @GetMapping("/")
    public String test(){
        return "login";
    }
}
