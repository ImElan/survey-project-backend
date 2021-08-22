package com.accolite.survey.DAO;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.accolite.survey.entity.MailData;




	
	public interface MailDataDAO extends MongoRepository<MailData, String>{
		
		@Query("{date:?0}")
		List<MailData>	getByDate(String d);

	}

