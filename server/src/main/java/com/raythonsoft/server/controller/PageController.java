package com.raythonsoft.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Anur IjuoKaruKas on 2018/2/7.
 * Description :
 */
@Controller
@RequestMapping
public class PageController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/home")
    public String home() {
        return "login_success";
    }
}
