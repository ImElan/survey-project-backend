package com.accolite.survey.DAO;

import org.springframework.stereotype.Repository;

import com.accolite.survey.entity.demo_data;


import org.springframework.data.mongodb.repository.MongoRepository;


//Here, we are extending all the methods of mongoDB
@Repository
public interface SurveyRepository extends MongoRepository<demo_data, String> {	
	
}
