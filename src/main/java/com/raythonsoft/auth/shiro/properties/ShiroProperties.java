package com.raythonsoft.auth.shiro.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Anur IjuoKaruKas on 2018/1/10.
 * Description :
 */
@Data
@ConfigurationProperties(prefix = "shiro")
public class ShiroProperties {
    private String sessionIdCookie;

    private String unauthorizedUrl;

    private String loginSuccessUrl;

    private String loginUrl;

    private Integer sessionTimeout;

    private Integer rememberMeTimeout;

    private String rememberMeCipherKey;
}
