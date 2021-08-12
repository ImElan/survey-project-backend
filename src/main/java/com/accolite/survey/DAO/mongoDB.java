package com.accolite.survey.DAO;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

//Enabling mongorepo/methods to the surveyrepo class

@EnableMongoRepositories(basePackageClasses = SurveyRepository.class)
@Configuration
public class mongoDB {

}