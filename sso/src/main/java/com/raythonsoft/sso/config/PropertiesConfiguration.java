package com.raythonsoft.sso.config;

import com.raythonsoft.sso.properties.ShiroProperties;
import com.raythonsoft.sso.properties.SsoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Anur IjuoKaruKas on 2018/4/9.
 * Description :
 */
@Configuration
public class PropertiesConfiguration {

    @Bean
    public ShiroProperties shiroProperties() {
        return new ShiroProperties();
    }

    @Bean
    public SsoProperties ssoProperties() {
        return new SsoProperties();
    }
}
