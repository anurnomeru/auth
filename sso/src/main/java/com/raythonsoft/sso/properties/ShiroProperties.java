package com.raythonsoft.sso.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Anur IjuoKaruKas on 2018/1/10.
 * Description :
 */
@Data
@ConfigurationProperties(prefix = "shiro")
public class ShiroProperties {

    private String authShiroSessionId;

    private String authClientSessionId;// client局部会话 <Set>

    private String authClientSessionIds;// 在code下注册局部会话 <Set>

    private String authServerSessionId;// server全局会话 <Set>

    private String authServerSessionIds;// 在code下注册全局会话 <List>

    private String authCheckCode;// check code校验值 <Set>

    private String unauthorizedUrl;// 无权限跳转

    private String loginSuccessUrl;// 登陆成功跳转

    private String loginUrl;// 登录页

    private Integer sessionTimeout;// 超时时间

    private Integer rememberMeTimeout;// 记住我超时时间

    private String rememberMeCipherKey;// 记住我加密密钥
}
