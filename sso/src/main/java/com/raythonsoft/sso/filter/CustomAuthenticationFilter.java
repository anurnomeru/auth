package com.raythonsoft.sso.filter;

import com.github.pagehelper.util.StringUtil;
import com.raythonsoft.common.constant.AuthConstant;
import com.raythonsoft.common.constant.SsoTypeEnum;
import com.raythonsoft.common.util.PropertiesFileUtil;
import com.raythonsoft.common.util.RequestParameterUtil;
import com.raythonsoft.sso.repository.SessionOperationRepository;
import lombok.extern.log4j.Log4j;
import org.apache.http.HttpClientConnection;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


/**
 * Created by Anur IjuoKaruKas on 2018/1/9.
 * Description : 认证 filter
 */
@Log4j
public class CustomAuthenticationFilter extends AuthenticationFilter {

    @Autowired
    private SessionOperationRepository sessionOperationRepository;

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
                .getInstance(AuthConstant.SSO_PROPERTY.getConfigName())
                .get(AuthConstant.SSO_PROPERTY.SSO_PROPERTY_TYPE);

        // 存入当前请求是客户端还是服务器端
        session.setAttribute(AuthConstant.SSO_PROPERTY.SSO_PROPERTY_TYPE, ssoType);

        if (SsoTypeEnum.CLIENT.name().equals(ssoType)) {
            return validateClient(request, response);
        }
        if (SsoTypeEnum.SERVER.name().equals(ssoType)) {

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
        String cacheCode = sessionOperationRepository.getCodeByGenningSessionId(sessionOperationRepository.genShiroSessionId(sessionId));
        if (cacheCode != null) {
            // 更新session有效期
            sessionOperationRepository.setCodeByGenningSessionId(
                    sessionOperationRepository.genClientSessionId(sessionId),
                    cacheCode,
                    timeOut,
                    TimeUnit.SECONDS);

            sessionOperationRepository.expireCodeByGenningSessionId(
                    sessionOperationRepository.genClientSessionIds(sessionId),
                    timeOut,
                    TimeUnit.SECONDS);

            if (null != request.getParameter("code")) {
                String backUrl = RequestParameterUtil.getUrlWithOutCodeAndName(WebUtils.toHttp(request));//fixme webUtils是个好东西
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

        // 判断认证中心是否有code
        String code = request.getParameter("sso_code");
        if (StringUtil.isNotEmpty(code)) {
            StringBuffer ssoServerUrl = new StringBuffer(PropertiesFileUtil
                    .getInstance(AuthConstant.SSO_PROPERTY.getConfigName())
                    .get(AuthConstant.SSO_PROPERTY.SSO_PROPERTY_SERVER_URL));

        }
    }
}
