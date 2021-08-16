package com.accolite.survey.DAO;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.accolite.survey.entity.Responses;

import java.util.List;

public interface ResponsesDAO extends MongoRepository<Responses,String>{
	@Query("{'formid': ?0}")
    public List<Responses> findByFormId(String formid);
}
