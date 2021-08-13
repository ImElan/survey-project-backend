package com.accolite.survey.DAO;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.accolite.survey.entity.Form;
public interface FormDAO extends MongoRepository<Form,String>{
}
