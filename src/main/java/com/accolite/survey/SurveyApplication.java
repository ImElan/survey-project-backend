package com.accolite.survey;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.accolite.survey.service.FormService;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = FormService.class)
public class SurveyApplication {
	public static void main(String[] args) {
		SpringApplication.run(SurveyApplication.class, args);
	}
}
