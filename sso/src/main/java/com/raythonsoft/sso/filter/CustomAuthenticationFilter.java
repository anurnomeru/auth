package com.raythonsoft.sso.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.raythonsoft.common.constant.AuthConstant;
import com.raythonsoft.common.constant.SsoTypeEnum;
import com.raythonsoft.common.model.Result;
import com.raythonsoft.common.model.ResultCode;
import com.raythonsoft.common.model.SsoCode;
import com.raythonsoft.common.util.PropertiesFileUtil;
import com.raythonsoft.sso.properties.SsoProperties;
import com.raythonsoft.sso.util.UrlUtil;
import lombok.extern.log4j.Log4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


/**
 * Created by Anur IjuoKaruKas on 2018/1/9.
 * Description : 认证 filter
 */
@Log4j
@Component
public class CustomAuthenticationFilter extends FormAuthenticationFilter {

    @Autowired
    private SsoProperties ssoProperties;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (this.isLoginRequest(request, response)) {
            if (this.isLoginSubmission(request, response)) {
                return this.executeLogin(request, response);
            } else {
                return true;
            }
        } else {
            log.info("用户没有登陆，需要进行认证。");
            this.saveRequest(request);
            if (ssoProperties.getType().equals(SsoTypeEnum.SERVER.getName())) {
                log.info("用户正访问验证中心。");
                WebUtils.issueRedirect(request, response, ssoProperties.getServerUrl());
            } else {
                log.info("用户正访问客户端。");
                return validateClient(request, response);
            }
            return false;
        }
    }


    private boolean validateClient(ServletRequest request, ServletResponse response) throws Exception {
        String checkCode = request.getParameter("checkCode");
        String userName = request.getParameter("username");
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;

            if (checkCode != null) {
                log.info("有code参数，可进行登陆");
                if (validateCode(checkCode)) {
                    log.info("认证成功，进行无密登陆");
                    getSubject(request, response).login(new UsernamePasswordToken(userName, ""));
                    WebUtils.issueRedirect(request, response, UrlUtil.getUrlWithOutCodeAndName(httpServletRequest));
                    return true;
                } else {
                    log.info("认证失败");
                    WebUtils.issueRedirect(request, response, String.format("%s?backUrl=%s", ssoProperties.getServerUrl(), UrlUtil.getEncodeUrlWithParam(httpServletRequest)));
                    return false;
                }
            }

            // 没登陆的情况下，将请求转发到认证中心
            log.info("用户未拥有code参数，需访问验证中心");
            WebUtils.issueRedirect(request, response, String.format("%s?backUrl=%s", ssoProperties.getServerUrl(), UrlUtil.getEncodeUrlWithParam(httpServletRequest)));
        }
        return false;
    }

    private boolean validateCode(String checkCode) {
        // 获取回调地址
        String ssoServerUrl = ssoProperties.getServerUrl();

        // 校验code
        HttpEntity<SsoCode> restTemplateRequestEntity = new HttpEntity<>(SsoCode.builder().checkCode(checkCode).build(), null);
        ResponseEntity<JSONObject> restTemplateResponse = restTemplate.exchange(ssoServerUrl + AuthConstant.URL_SSO_CODE,
                HttpMethod.POST,
                restTemplateRequestEntity,
                JSONObject.class);

        String restTemplateResponseJsonStr = restTemplateResponse.getBody().toJSONString();
        Result<String> result = JSON.parseObject(restTemplateResponseJsonStr,
                new TypeReference<Result<String>>() {
                });

        if (result.getCode() == ResultCode.SUCCESS.code && checkCode.equals(result.getData())) {// 如果code校验成功
            return true;
        }

        return false;
    }
}
