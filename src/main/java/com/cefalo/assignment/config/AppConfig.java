package com.cefalo.assignment.config;

import com.cefalo.assignment.model.business.StoryPropertiesConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@ComponentScan(basePackages = "com.cefalo.assignment")
@EnableSwagger2
@EnableConfigurationProperties(StoryPropertiesConfig.class)
public class AppConfig {

}
