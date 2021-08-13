package com.accolite.survey.DAO;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackageClasses = FormDAO.class)
@Configuration
public class MongoConfig {

}
