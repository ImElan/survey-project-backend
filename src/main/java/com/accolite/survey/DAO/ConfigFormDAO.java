package com.accolite.survey.DAO;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.accolite.survey.entity.SurveyFormConfig;

public interface ConfigFormDAO extends MongoRepository<SurveyFormConfig,String>{
	
}
