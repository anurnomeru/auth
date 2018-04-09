package com.raythonsoft.server.controller;

import com.raythonsoft.common.util.StringUtils;
import com.raythonsoft.sso.common.SsoConstant;
import com.raythonsoft.sso.service.SessionService;
import com.raythonsoft.sso.util.UrlUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private SessionService sessionService;

    @GetMapping("/")
    public String index(String backUrl) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        // 如果请求来自客户端
        if (StringUtils.isNotEmpty(backUrl)) {
            if (subject.isAuthenticated()) {
                // 已经登陆，则直接带着code 回跳
//                return String.format("redirect:%s", UrlUtil.addParam(backUrl, "checkCode", sessionService.getCheckCodeByGenCheckCode(session)));
            } else {
                // 没有登陆，则去登陆
                subject.getSession().setAttribute(SsoConstant.BACK_URL, backUrl);
            }
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "index";
    }

    @GetMapping("/home")
    public String home() {
        return "login_success";
    }
}
