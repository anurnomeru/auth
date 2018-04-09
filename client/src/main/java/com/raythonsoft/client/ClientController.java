package com.raythonsoft.client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Anur IjuoKaruKas on 2018/4/9.
 * Description :
 */
@RestController
public class ClientController {

    @GetMapping("client")
    public String test() {
        return "client";
    }
}
