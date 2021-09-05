package com.accolite.survey.DAO;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.accolite.survey.entity.Preview;

public interface PreviewFormDAO extends MongoRepository<Preview,String>{

}
