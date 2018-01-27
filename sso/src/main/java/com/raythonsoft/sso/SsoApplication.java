package com.raythonsoft.sso;

import com.raythonsoft.sso.properties.ShiroProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@EnableAutoConfiguration
@SpringBootApplication
//        (scanBasePackages = {"com.raythonsoft.sso.*","com.raythonsoft.sso.*"})
public class SsoApplication {
    @Bean
    public ShiroProperties shiroProperties() {
        return new ShiroProperties();
    }

    public static void main(String[] args) {
        SpringApplication.run(SsoApplication.class, args);
    }
}
