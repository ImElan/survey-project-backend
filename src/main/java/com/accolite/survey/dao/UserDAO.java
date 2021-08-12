package com.accolite.survey.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.accolite.survey.entity.User;

public interface UserDAO extends MongoRepository<User, String>{
	
	@Query("{'employeeId':?0,'password':?1}")
	void findByEmployeeIdAndPassword(String employeeId, String password);

}
