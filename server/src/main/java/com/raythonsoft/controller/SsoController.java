package com.raythonsoft.controller;

import com.raythonsoft.common.constant.AuthConstant;
import com.raythonsoft.common.model.Result;
import com.raythonsoft.common.model.ResultCode;
import com.raythonsoft.common.util.PropertiesFileUtil;
import com.raythonsoft.common.util.ResultGenerator;
import com.raythonsoft.common.util.StringUtils;
import com.raythonsoft.sso.exception.ServiceException;
import com.raythonsoft.sso.model.AuthSystem;
import com.raythonsoft.sso.repository.CodeRedisRepository;
import com.raythonsoft.sso.repository.SessionIdGenerator;
import com.raythonsoft.sso.repository.SessionOperationRepository;
import com.raythonsoft.sso.service.AuthSystemService;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by Anur IjuoKaruKas on 2018/1/29.
 * Description :
 */
@Controller
@RequestMapping("/sso")
@Api(value = "单点登录管理")
@Log4j
public class SsoController {

    @Autowired
    private SessionIdGenerator sessionIdGenerator;

    @Autowired
    private CodeRedisRepository codeRedisRepository;

    @Autowired
    private SessionOperationRepository sessionOperationRepository;

    @Autowired
    private AuthSystemService authSystemService;

    @ApiOperation(value = "认证中心首页")
    public String index(@RequestParam String appid, String backUrl) throws UnsupportedEncodingException {
        AuthSystem authSystem = authSystemService.findBy("appid", appid);
        if (authSystem == null) {
            throw new ServiceException("Appid not exist");
        }
        return "redirect:/sso/login?backUrl=" + URLEncoder.encode(backUrl, "utf-8");
    }

    @ApiOperation(value = "登陆")
    @GetMapping("/login")
    public String login(String backUrl) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        String serverSessionId = String.valueOf(session.getId());

        // 全局会话checkCode
        String checkCode = codeRedisRepository.getCheckCodeByGenningSessionId(sessionIdGenerator.genServerCode(serverSessionId));// 全局会话session

        // 如果全局会话session已经登陆
        if (StringUtils.isNotEmpty(checkCode)) {
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

    @ApiOperation(value = "管理系统登陆")
    @PostMapping("/server/login")
    public String serverLogin(@RequestParam String username, @RequestParam String password, @RequestParam(required = false, defaultValue = "false") boolean rememberMe, String backUrl) {
        Result result = this.login(username, password, rememberMe, backUrl);
        if (result.getCode() == ResultCode.SUCCESS.code) {
            return "redirect:../../index.html";
        }
        return "/";
    }

    @ApiOperation(value = "登陆")
    @PostMapping("/login")
    @ResponseBody
    public Result login(@RequestParam String username, @RequestParam String password, @RequestParam(required = false, defaultValue = "false") boolean rememberMe, String backUrl) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        String serverSessionId = String.valueOf(session.getId());

        String ossCode = codeRedisRepository.getCheckCodeByGenningSessionId(sessionIdGenerator.genServerCode(serverSessionId));// 全局会话session

        // 如果还没有登陆
        if (StringUtils.isEmpty(ossCode)) {
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
            usernamePasswordToken.setRememberMe(rememberMe);

            subject.login(usernamePasswordToken);

            // 更改状态为：在线
            sessionOperationRepository.updateShiroSessionStatus(serverSessionId, OnlineStatusEnum.ON_LINE);
            // 将这个sessionId 存入全局会话
            sessionOperationRepository.leftPushIntoServerSessionId(serverSessionId);

            String checkCode = String.valueOf(UUID.randomUUID());

            // 维护全局会话的code
            codeRedisRepository.setCheckCodeIntoGenningSessionId(sessionIdGenerator.genServerSessionId(serverSessionId), checkCode, (int) session.getTimeout() / 1000, TimeUnit.SECONDS);
            // 初始化 code 的校验值
            codeRedisRepository.setCheckCode(checkCode, (int) session.getTimeout() / 1000, TimeUnit.SECONDS);
        }
        if (StringUtils.isEmpty(backUrl)) {
            AuthSystem authSystem = authSystemService.findBy("appid", PropertiesFileUtil.getInstance(AuthConstant.SSO_PROPERTY.getPropertyFileName()).get(AuthConstant.SSO_PROPERTY.SSO_PROPERTY_APPID));
            backUrl = (authSystem == null ? "/" : authSystem.getBasePath());
        }
        return ResultGenerator.genSuccessResult(backUrl);
    }

    @ApiOperation(value = "校验code")
    @PostMapping(value = "/code")
    @ResponseBody
    public Result code(String checkCode) {
        String checkCodeFromRedis = codeRedisRepository.getCheckCodeByGenningSessionId(sessionIdGenerator.genServerCode(checkCode));
        if (StringUtils.isEmpty(checkCodeFromRedis) || !checkCodeFromRedis.equals(checkCode)) {
            return ResultGenerator.genFailResult("CheckCode Validate Fail!");
        }
        return ResultGenerator.genSuccessResult(checkCodeFromRedis);
    }

    @ApiOperation(value = "退出登录")
    @GetMapping(value = "/logout")
    public String logout(HttpServletRequest request) {
        SecurityUtils.getSubject().logout();
        String redirectUrl = request.getHeader("Referer");
        return "redirect:" + (redirectUrl == null ? "/" : redirectUrl);
    }
}
