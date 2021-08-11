package com.accolite.survey.DAO;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.accolite.survey.entity.Forms;


public interface FormsRepository extends MongoRepository<Forms, String>{
	
}
