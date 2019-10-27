package com.cefalo.assignment.config;

import com.cefalo.assignment.model.business.StoryPropertiesConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.cefalo.assignment")
@EnableConfigurationProperties(StoryPropertiesConfig.class)
public class AppConfig {

   /* @Bean(value = "storyPropertiesConfig")
    public StoryPropertiesConfig getStoryPropertiesConfig(){
        return new StoryPropertiesConfig();
    }*/
}
