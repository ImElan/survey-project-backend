package com.accolite.survey.DAO;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import com.accolite.survey.entity.Responses;
import java.util.List;
import java.util.Optional;

public interface ResponsesDAO extends MongoRepository<Responses,String>{
	@Query("{'formid': ?0}")
    Optional<Responses> findByFormId(String formid);
	
	@Query("{formid: ?0,userid:?1}")
    Optional<Responses> findByFormIdandUserId(String formid,String userid);
}
