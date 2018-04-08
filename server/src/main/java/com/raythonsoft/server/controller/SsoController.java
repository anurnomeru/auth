package com.raythonsoft.server.controller;

import com.raythonsoft.common.util.StringUtils;
import com.raythonsoft.sso.service.SessionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Created by Anur IjuoKaruKas on 2018/4/7.
 * Description :
 */
@Controller
@RequestMapping("/sso")
@Api
@Log4j
public class SsoController {

    private final SessionService sessionService;

    @Autowired
    public SsoController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    /**
     * @param username
     * @param password
     * @param redirectUrl 跳回子项目
     * @return
     */
    @ApiOperation(value = "登陆 = sso中心")
    @PostMapping("/login")
    public String login4SsoServer(@RequestParam String username, @RequestParam String password, String redirectUrl) {

        // 首先获取出当前请求的sessionId
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();

        String checkCode = "";
        if (!sessionService.isEffective(session)) {// 如果无效
            // 则进行登陆
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
            usernamePasswordToken.setRememberMe(false);

            // 错误则不进行跳转
            try {
                subject.login(usernamePasswordToken);
            } catch (AuthenticationException e) {
                return "redirect:/";
            }

            // 将session有效化
            checkCode = sessionService.sessionEffective(session);
        }

        if (StringUtils.isEmpty(redirectUrl)) {
            return "redirect:/home";
        } else {
            String result = String.format("redirect:%s?checkCode=%s", redirectUrl, checkCode);
            return result;
        }
    }
}
