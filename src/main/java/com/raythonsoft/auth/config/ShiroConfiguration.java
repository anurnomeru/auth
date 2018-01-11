package com.raythonsoft.auth.config;

import com.raythonsoft.auth.shiro.properties.ShiroProperties;
import com.raythonsoft.auth.shiro.session.CustomSessionFactory;
import com.raythonsoft.auth.shiro.filter.CustomAccessControlFilter;
import com.raythonsoft.auth.shiro.filter.CustomAuthenticationFilter;
import com.raythonsoft.auth.shiro.listener.CustomSessionListener;
import com.raythonsoft.auth.shiro.realm.CustomRealm;
import com.raythonsoft.auth.shiro.session.SessionDao;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anur IjuoKaruKas on 2018/1/9.
 * Description :
 */
@Configuration
public class ShiroConfiguration {

    /**
     * 自定义属性
     *
     * @return
     */
    @Bean
    public ShiroProperties shiroProperties() {
        return new ShiroProperties();
    }

    /**
     * web 过滤器
     *
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl(shiroProperties().getLoginUrl());
        shiroFilterFactoryBean.setSuccessUrl(shiroProperties().getLoginSuccessUrl());
        shiroFilterFactoryBean.setUnauthorizedUrl(shiroProperties().getUnauthorizedUrl());
        return shiroFilterFactoryBean;
    }

    /**
     * Authentication 过滤器
     *
     * @return
     */
    @Bean
    public AuthenticationFilter authenticationFilter() {
        return new CustomAuthenticationFilter();
    }

    /**
     * Access 过滤器，用于会话的强制关闭
     *
     * @return
     */
    @Bean
    public AccessControlFilter accessControlFilter() {
        return new CustomAccessControlFilter();
    }

    /**
     * 安全管理器
     *
     * @return
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(authorizingRealm());// realm，用于自定义认证与授权
        securityManager.setSessionManager(sessionManager());
        securityManager.setRememberMeManager(cookieRememberMeManager());
        return securityManager;
    }

    /**
     * 自定义realm
     *
     * @return
     */
    @Bean
    public AuthorizingRealm authorizingRealm() {
        return new CustomRealm();
    }

    /**
     * 会话管理器
     *
     * @return
     */
    @Bean
    public DefaultWebSessionManager sessionManager() {
        // 废弃servlet的会话管理
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        // 全局session超时时间
        sessionManager.setGlobalSessionTimeout(shiroProperties().getSessionTimeout());

        // sessionDao
        sessionManager.setSessionDAO(sessionDAO());
        // 在某些servlet容器中，默认使用JSESSIONID Cookie维护会话，这里使用自己的会话机制
        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setSessionIdCookie(sessionIdCookie());
        sessionManager.setSessionValidationSchedulerEnabled(false);

        List<SessionListener> sessionListenerList = new ArrayList<>();
        sessionListenerList.add(sessionListener());
        sessionManager.setSessionListeners(sessionListenerList);

        sessionManager.setSessionFactory(sessionFactory());
        return sessionManager;
    }

    /**
     * 会话DAO
     *
     * @return
     */
    @Bean
    public SessionDAO sessionDAO() {
        return new SessionDao();
    }

    /**
     * 会话Cookie设置
     *
     * @return
     */
    @Bean
    public SimpleCookie sessionIdCookie() {
        SimpleCookie simpleCookie = new SimpleCookie();
        // 不会暴露给客户端
        simpleCookie.setHttpOnly(true);
        // sessionId cookie（永久）
        simpleCookie.setMaxAge(-1);
        // Cookie名称
        simpleCookie.setName(shiroProperties().getAuthClientSessionId());
        return simpleCookie;
    }

    /**
     * 会话监听器
     *
     * @return
     */
    @Bean
    public SessionListener sessionListener() {
        return new CustomSessionListener();
    }

    /**
     * 会话工厂
     *
     * @return
     */
    @Bean
    public SessionFactory sessionFactory() {
        return new CustomSessionFactory();
    }

    /**
     * 记住我管理器
     *
     * @return
     */
    @Bean
    public CookieRememberMeManager cookieRememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        // rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度（128 256 512 位）
        cookieRememberMeManager.setCipherKey(Base64.decode(shiroProperties().getRememberMeCipherKey()));
        cookieRememberMeManager.setCookie(sessionIdCookie());
        return cookieRememberMeManager;
    }

    /**
     * 记住我缓存cookie
     *
     * @return
     */
    @Bean
    public SimpleCookie rememberMeCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        // 不会暴露给客户端
        simpleCookie.setHttpOnly(true);
        // 记住我cookie生效时间
        simpleCookie.setMaxAge(shiroProperties().getRememberMeTimeout());
        return simpleCookie;
    }

    /**
     * 设置SecurityUtils，相当于调用SecurityUtils.setSecurityManager(securityManager)
     *
     * @return
     */
    @Bean
    public MethodInvokingFactoryBean methodInvokingFactoryBean() {
        MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
        methodInvokingFactoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        methodInvokingFactoryBean.setArguments(securityManager());
        return methodInvokingFactoryBean;
    }

    /**
     * 添加 @RequiresPermissions 支持
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * shiro生命周期
     *
     * @return
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
//
//    @Bean
//    public HashedCredentialsMatcher hashedCredentialsMatcher() {
//        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
//
//        hashedCredentialsMatcher.setHashAlgorithmName("md5");// 散列算法
//        hashedCredentialsMatcher.setHashIterations(2);// 散列的次数
//
//        return hashedCredentialsMatcher;
//    }
}
