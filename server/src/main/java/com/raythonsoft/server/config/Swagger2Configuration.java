package com.raythonsoft.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by Anur IjuoKaruKas on 2017/12/13.
 * Description :
 */
@Configuration
@EnableSwagger2
@EnableWebMvc
public class Swagger2Configuration {

    @Bean public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("api4web")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.raythonsoft.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Auth Service")
                .description("Auth Service Swagger2 Api，http://www.raythonsoft.com")
                .termsOfServiceUrl("http://www.raythonsoft.com")
                .version("1.0")
                .build();
    }
}
