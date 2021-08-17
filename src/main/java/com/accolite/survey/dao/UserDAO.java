package com.accolite.survey.DAO;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.accolite.survey.entity.User;

@Repository
public interface UserDAO extends MongoRepository<User, String>{

}
