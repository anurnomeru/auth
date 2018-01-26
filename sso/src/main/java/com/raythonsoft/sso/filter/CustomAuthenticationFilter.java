package com.raythonsoft.sso.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.pagehelper.util.StringUtil;
import com.raythonsoft.common.constant.AuthConstant;
import com.raythonsoft.common.constant.SsoTypeEnum;
import com.raythonsoft.common.model.Result;
import com.raythonsoft.common.model.ResultCode;
import com.raythonsoft.common.model.SsoCode;
import com.raythonsoft.common.util.PropertiesFileUtil;
import com.raythonsoft.common.util.RequestParameterUtil;
import com.raythonsoft.sso.repository.CodeRedisRepository;
import com.raythonsoft.sso.repository.SessionIdGenerator;
import com.raythonsoft.sso.repository.SessionOperationRepository;
import lombok.extern.log4j.Log4j;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;


/**
 * Created by Anur IjuoKaruKas on 2018/1/9.
 * Description : 认证 filter
 */
@Log4j
public class CustomAuthenticationFilter extends AuthenticationFilter {

    private static RestTemplate restTemplate;

    private static SessionIdGenerator sessionIdGenerator;

    private static CodeRedisRepository codeRedisRepository;


    @Autowired
    private SessionIdGenerator sessionIdGeneratorAutowired;

    @Autowired
    private CodeRedisRepository codeRedisRepositoryAutowired;

    @Autowired
    private SessionOperationRepository sessionOperationRepositoryAutowired;

    @Autowired
    private RestTemplate restTemplateAutowired;

    @PostConstruct
    public void beforeInit() {
        sessionIdGenerator = sessionIdGeneratorAutowired;
        codeRedisRepository = codeRedisRepositoryAutowired;
        restTemplate = restTemplateAutowired;
    }

    /**
     * 是否通过认证
     *
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        Subject subject = getSubject(request, response);
        Session session = subject.getSession();

        // 判断请求类型
        String ssoType = PropertiesFileUtil
                .getInstance(AuthConstant.SSO_PROPERTY.getPropertyFileName())
                .get(AuthConstant.SSO_PROPERTY.SSO_PROPERTY_TYPE);

        // 存入当前请求是客户端还是服务器端
        session.setAttribute(AuthConstant.SSO_PROPERTY.SSO_PROPERTY_TYPE, ssoType);

        if (SsoTypeEnum.CLIENT.name().equals(ssoType)) {
            return validateClient(request, response);
        }
        if (SsoTypeEnum.SERVER.name().equals(ssoType)) {
            return subject.isAuthenticated();
        }
        return false;
    }

    /**
     * 认证通过后，做什么
     *
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        // 获取回调地址
        StringBuilder ssoServerUrl = PropertiesFileUtil
                .getInstance(AuthConstant.SSO_PROPERTY.getPropertyFileName())
                .get(AuthConstant.SSO_PROPERTY.SSO_PROPERTY_SERVER_URL);

        // 判断请求类型
        String ssoType = PropertiesFileUtil
                .getInstance(AuthConstant.SSO_PROPERTY.getPropertyFileName())
                .get(AuthConstant.SSO_PROPERTY.SSO_PROPERTY_TYPE);

        if (SsoTypeEnum.SERVER.name().equals(ssoType)) {
            WebUtils.toHttp(servletResponse).sendRedirect(String.valueOf(ssoServerUrl.append(AuthConstant.URL_SSO_LOGIN)));
            return false;
        }

        ssoServerUrl.append(String.format(AuthConstant.URL_SSO_INDEX + "?appid=%s"
                , PropertiesFileUtil
                        .getInstance(AuthConstant.SSO_PROPERTY.getPropertyFileName())
                        .get(AuthConstant.SSO_PROPERTY.SSO_PROPERTY_TYPE)));

        // 回跳地址
        HttpServletRequest httpServletRequest = WebUtils.toHttp(servletRequest);

        StringBuffer backUrl = httpServletRequest.getRequestURL();// 当前url
        String queryString = httpServletRequest.getQueryString();// 加参数
        if (StringUtil.isNotEmpty(queryString)) {
            backUrl.append("?").append(queryString);
        }
        ssoServerUrl.append("&backUrl=").append(URLEncoder.encode(backUrl.toString(), "utf-8"));
        WebUtils.toHttp(servletResponse).sendRedirect(ssoServerUrl.toString());// 两个参数，一个appid 一个backUrl
        return false;
    }

    /**
     * 客户端验证
     *
     * @param request
     * @param response
     * @return
     */
    private boolean validateClient(ServletRequest request, ServletResponse response) {
        Subject subject = getSubject(request, response);
        Session session = subject.getSession();
        String sessionId = String.valueOf(session.getId());
        int timeOut = (int) session.getTimeout() / 1000;

        // 判断局部会话是否已经登陆
        String cacheCode = codeRedisRepository.getCodeByGenningSessionId(sessionIdGenerator.genShiroSessionId(sessionId));
        if (cacheCode != null) {// 如果已经登陆过
            // 更新session有效期
            codeRedisRepository.setCodeByGenningSessionId(// 更新该session在局部会话的有效期
                    sessionIdGenerator.genClientSessionId(sessionId),
                    cacheCode,
                    timeOut,
                    TimeUnit.SECONDS);

            codeRedisRepository.expireCode(// 更新该session所属 code下局部会话 Set 中
                    sessionIdGenerator.genClientSessionIdsCodeParamCode(cacheCode),
                    timeOut,
                    TimeUnit.SECONDS);

            if (null != request.getParameter(AuthConstant.REQUEST_PARAM_CODE)) {
                String backUrl = RequestParameterUtil.getUrlWithOutCodeAndName(WebUtils.toHttp(request));// 移除sso_code和sso_username //fixme webUtils是个好东西
                HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
                try {
                    httpServletResponse.sendRedirect(backUrl);
                } catch (IOException e) {
                    log.error("Login success, bug redirect ERROR: ", e);
                }
            } else {
                return true;
            }
        }

        // 判断认证中心是否有sso_code
        String ssoCode = request.getParameter(AuthConstant.REQUEST_PARAM_OSS_CODE);
        if (StringUtil.isNotEmpty(ssoCode)) {// 如果没有登陆过，但是请求带了sso_code
            // 获取回调地址
            String ssoServerUrl = PropertiesFileUtil
                    .getInstance(AuthConstant.SSO_PROPERTY.getPropertyFileName())
                    .get(AuthConstant.SSO_PROPERTY.SSO_PROPERTY_SERVER_URL);

            // 校验code
            HttpEntity<SsoCode> restTemplateRequestEntity = new HttpEntity<>(SsoCode.builder().SsoCode(ssoCode).build(), null);
            ResponseEntity<JSONObject> restTemplateResponse = restTemplate.exchange(ssoServerUrl + AuthConstant.URL_SSO_CODE,
                    HttpMethod.POST,
                    restTemplateRequestEntity,
                    JSONObject.class);

            String restTemplateResponseJsonStr = restTemplateResponse.getBody().toJSONString();
            Result<String> result = JSON.parseObject(restTemplateResponseJsonStr,
                    new TypeReference<Result<String>>() {
                    });

            if (result.getCode() == ResultCode.SUCCESS.code && ssoCode.equals(result.getData())) {// 如果code校验成功
                codeRedisRepository.setCodeByGenningSessionId(// 注册sessionId到局部会话
                        sessionIdGenerator.genClientSessionId(sessionId),
                        ssoCode,
                        timeOut,
                        TimeUnit.SECONDS
                );
                codeRedisRepository.saddCode(// 将sessionId保存到该code下的局部会话 Set 中
                        sessionIdGenerator.genClientSessionIdsCodeParamCode(ssoCode),
                        sessionId,
                        timeOut,
                        TimeUnit.SECONDS);

                codeRedisRepository.scardCode(sessionIdGenerator.genClientSessionIdsCodeParamCode(ssoCode));// 打印 code 下注册的系统

                String ssoUsername = request.getParameter(AuthConstant.REQUEST_PARAM_OSS_USERNAME);
                String backUrl = RequestParameterUtil.getUrlWithOutCodeAndName(WebUtils.toHttp(request));// 移除sso_code和sso_username
                try {
                    // 无密认证
                    subject.login(new UsernamePasswordToken(ssoUsername, ""));
                    WebUtils.toHttp(response).sendRedirect(backUrl);
                    return true;
                } catch (IOException e) {
                    log.error("Resolve ssoCode success, bug redirect ERROR: ", e);
                }
            } else {
                log.warn("Resolve ssoCode fail: " + result.getData());
            }
        }
        return false;
    }
}
