package com.accolite.survey.entity.respository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.accolite.survey.entity.Form;


public interface FormRepository extends MongoRepository <Form,String>{

}
