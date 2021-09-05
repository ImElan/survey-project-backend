package com.accolite.survey.service;

import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;

import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.accolite.survey.DAO.PreviewFormDAO;
import com.accolite.survey.DAO.Auth.AuthDAOImplementation;
import com.accolite.survey.Model.TokenType;
import com.accolite.survey.entity.Preview;
import com.accolite.survey.entity.User;
import com.accolite.survey.entity.UserRoles;

@Service
public class PreviewService {

	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	AuthDAOImplementation authdao;
	@Autowired
	PreviewFormDAO previewdao;
	
	public String addResponse(Preview preview,String bearerToken){
//		User user = authdao.isAuthenticated(bearerToken,TokenType.ACCESS);
//		System.out.println(user);
//		UserRoles[] roles = {UserRoles.HR};
//		authdao.restrictTo(roles, user);
		
		System.out.println("\n\n\n"+preview+"\n\n\n");
		previewdao.save(preview);
		System.out.println(preview.getId().toString());
		return preview.getId().toString();
		
		

	}
	
	public Optional<Preview> getResponse(String id,String token)
	{
		return previewdao.findById(id);
	}

}
