package com.raythonsoft.controller;

import com.raythonsoft.common.constant.AuthConstant;
import com.raythonsoft.common.model.Result;
import com.raythonsoft.sso.model.Project;
import com.raythonsoft.sso.repository.CodeRedisRepository;
import com.raythonsoft.sso.repository.SessionIdGenerator;
import com.raythonsoft.sso.repository.SessionOperationRepository;
import com.raythonsoft.sso.service.ProjectService;
import com.raythonsoft.sso.session.OnlineStatusEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by Anur IjuoKaruKas on 2018/1/29.
 * Description :
 */
@Log4j
@Controller
@RequestMapping("/sso")
@Api(value = "单点登录管理")
public class SsoController {

    @Autowired
    private SessionIdGenerator sessionIdGenerator;

    @Autowired
    private CodeRedisRepository codeRedisRepository;

    @Autowired
    private SessionOperationRepository sessionOperationRepository;

    @Autowired
    private ProjectService projectService;

    @ApiOperation(value = "登陆")
    @GetMapping("/login")
    public String login(String backUrl) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        String serverSessionId = String.valueOf(session.getId());

        // 全局会话checkCode
        String checkCode = codeRedisRepository.getCheckCodeByGenningSessionId(sessionIdGenerator.genServerCode(serverSessionId));// 全局会话session

        // 如果全局会话session已经登陆
        if (!StringUtils.isEmpty(checkCode)) {
            String ossUsername = (String) subject.getPrincipal();
            if (StringUtils.isEmpty(backUrl)) {
                backUrl = "/";
            } else {
                if (backUrl.contains("?")) {
                    backUrl += "&";
                } else {
                    backUrl += "?";
                }
                backUrl += String.format("%s=%s&%s=%s", AuthConstant.REQUEST_PARAM_OSS_CODE, checkCode, AuthConstant.REQUEST_PARAM_OSS_USERNAME, ossUsername);
            }
            log.debug(String.format("认证中心账号通过，带code会跳：%s", backUrl));
            return "redirect:" + backUrl;
        }
        return "";/// FIXME: 2018/1/29 登陆界面
    }

    public Result login(@RequestParam String username, @RequestParam String password, @RequestParam(required = false, defaultValue = "false") boolean rememberMe, String backUrl) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        String serverSessionId = String.valueOf(session.getId());

        String ossCode = codeRedisRepository.getCheckCodeByGenningSessionId(sessionIdGenerator.genServerCode(serverSessionId));// 全局会话session

        // 如果全局会话session已经登陆
        if (StringUtils.isEmpty(ossCode)) {
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
            usernamePasswordToken.setRememberMe(rememberMe);

            subject.login(usernamePasswordToken);

            // 更改状态为：在线
            sessionOperationRepository.updateShiroSessionStatus(serverSessionId, OnlineStatusEnum.ON_LINE);
            // 维护全局会话的sessionId列表
            sessionOperationRepository.leftPushIntoServerSessionId(serverSessionId);

            String checkCode = String.valueOf(UUID.randomUUID());

            // 维护全局会话的code
            codeRedisRepository.setCheckCodeIntoGenningSessionId(sessionIdGenerator.genServerSessionId(serverSessionId), checkCode, (int) session.getTimeout() / 1000, TimeUnit.SECONDS);
            // 初始化 code 的校验值
            codeRedisRepository.setCheckCode(checkCode, (int) session.getTimeout() / 1000, TimeUnit.SECONDS);
        }
        if (!StringUtils.isEmpty(backUrl)) {
            Project project = projectService.findby
        }
    }


}
