package com.raythonsoft.server.controller;

import com.raythonsoft.common.model.Result;
import com.raythonsoft.common.model.SsoCode;
import com.raythonsoft.common.util.ResultGenerator;
import com.raythonsoft.common.util.StringUtils;
import com.raythonsoft.sso.common.SsoConstant;
import com.raythonsoft.sso.service.SessionService;
import com.raythonsoft.sso.util.UrlUtil;
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
import org.springframework.web.bind.annotation.*;


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
     * @return
     */
    @ApiOperation(value = "登陆 = sso中心")
    @PostMapping("/login")
    public String login4SsoServer(@RequestParam String username, @RequestParam String password) {

        // 首先获取出当前请求的sessionId
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();

        log.info("==> ServerSessionId: {}" + session.getId());

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

        String backUrl = String.valueOf(session.getAttribute(SsoConstant.BACK_URL));
        session.removeAttribute(SsoConstant.BACK_URL);

        if (StringUtils.isEmpty(backUrl)) {
            return "redirect:/home";
        } else {
            String url = String.format("redirect:%s", UrlUtil.addParam(backUrl, "checkCode", checkCode));
            System.out.println(url);
            return url;
        }
    }

    @ApiOperation(value = "校验checkCode")
    @PostMapping(value = "/code")
    @ResponseBody
    public Result code(@RequestBody SsoCode checkCode) {
        log.info("==> checkCode sessionId: {}" + SecurityUtils.getSubject().getSession().getId());

        String checkCodeFromRedis = sessionService.getCheckCodeByGenCheckCode(checkCode.getCheckCode());
        if (StringUtils.isEmpty(checkCodeFromRedis) || !checkCodeFromRedis.equals(checkCode.getCheckCode())) {
            return ResultGenerator.genFailResult("CheckCode Validate Fail!");
        }
        return ResultGenerator.genSuccessResult(checkCodeFromRedis);
    }
}
