package com.cefalo.assignment;

import com.cefalo.assignment.config.AppConfig;
import com.cefalo.assignment.model.business.StoryPropertiesConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@SpringBootApplication
@EnableConfigurationProperties(StoryPropertiesConfig.class)
public class AssignmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssignmentApplication.class, args);
		ApplicationContext applicationContext =
				new AnnotationConfigApplicationContext(AppConfig.class);
		StoryPropertiesConfig storyPropertiesConfig =
				(StoryPropertiesConfig)applicationContext.getBean("storyPropertiesConfig");

		System.out.println(storyPropertiesConfig);
	}
}
