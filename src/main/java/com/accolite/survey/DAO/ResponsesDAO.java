package com.accolite.survey.DAO;


import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.accolite.survey.entity.Responses;

public interface ResponsesDAO extends MongoRepository<Responses,String>{
	@Query("{'formid': ?0}")
    Optional<Responses> findByFormId(String formid);
}
