package com.raythonsoft.auth.shiro.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by Anur IjuoKaruKas on 2018/1/10.
 * Description :
 */
@Data
@PropertySource("classpath：application.properties")
@ConfigurationProperties(prefix = "shiro")
public class ShiroProperties {
    private String sessionId;

    private String unauthorizedUrl;

    private String loginSuccessUrl;

    private String loginUrl;

    private Integer sessionTimeout;

    private Integer rememberMeTimeout;

    private String rememberMeCipherKey;
}
