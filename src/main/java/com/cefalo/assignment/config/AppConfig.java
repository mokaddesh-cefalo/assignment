package com.cefalo.assignment.config;

import com.cefalo.assignment.model.business.StoryProperties;
import com.cefalo.assignment.utils.AuthenticationResponse;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@ComponentScan(basePackages = "com.cefalo.assignment")
@EnableSwagger2
@EnableConfigurationProperties(StoryProperties.class)
public class AppConfig {

    @Bean
    public Docket swaggerConfiguration(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.ant("/api/**"))
                .apis(RequestHandlerSelectors.basePackage("com.cefalo.assignment.controller"))
                .build()
                .apiInfo(apiDetails());
    }

    private ApiInfo apiDetails(){
        return new ApiInfo(
          "Story API",
          "Sample Api for learning Project",
          "1.0",
          "Free to use",
           new springfox.documentation.service.Contact("Mokaddesh Rashid", "localhost:8080", "mokaddesh@cefalo.com"),
                "Api license",
                "No license url",
                Collections.emptyList()
        );
    }

}
