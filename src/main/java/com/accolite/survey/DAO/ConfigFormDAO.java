package com.accolite.survey.DAO;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.accolite.survey.entity.SurveyFormConfig;

public interface ConfigFormDAO extends MongoRepository<SurveyFormConfig,String>{

}
