package com.raythonsoft.sso;

import com.raythonsoft.sso.properties.ShiroProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@SpringBootApplication(scanBasePackages = {"com.raythonsoft.repository"})
public class SsoApplication {
    @Bean
    @ConfigurationProperties(prefix = "shiro")
    public ShiroProperties shiroProperties() {
        return new ShiroProperties();
    }

    public static void main(String[] args) {
        SpringApplication.run(SsoApplication.class, args);
    }
}
