package com.accolite.survey.DAO;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.accolite.survey.entity.Responses;

@Repository
public interface ResponseDAO extends MongoRepository<Responses, String> {

}
