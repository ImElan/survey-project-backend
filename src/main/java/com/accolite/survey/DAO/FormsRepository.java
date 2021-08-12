package com.accolite.survey.DAO;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.accolite.survey.entity.Form;

// MongoRepository is inbuilt and it implements all the methods
public interface FormsRepository extends MongoRepository<Form, String>{
	
}
