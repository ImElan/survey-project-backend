package com.accolite.survey.DAO;


import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.accolite.survey.entity.Form;


public interface FormDAO extends MongoRepository<Form,String>{
	@Query("{'createdBy':?0}")
	public List<Form> getAllForm(String createdBy);
}
