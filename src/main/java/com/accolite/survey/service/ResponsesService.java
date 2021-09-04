package com.accolite.survey.service;

import java.util.List;

import javax.mail.MessagingException;

import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.web.bind.annotation.RequestBody;

import com.accolite.survey.controller.MyException;
import com.accolite.survey.entity.Responses;

public interface ResponsesService {

	public Responses addResponse(Responses response,String bearerToken) throws MyException, MessagingException;

	public List<Responses> getAllResponses(String bearerToken);

	public List<Responses> getResponseByFormId(String formid,String bearerToken);
	
	public Responses check(String user_id, String form_id) throws MyException;
	
	public Sheet createResponseCopy(Responses response, String title);
	
	public String sendEmailWithAttachment(String toEmail, String userName, String title, Sheet attachment) throws MessagingException;
	
	public Responses updateResponse(Responses responses, String bearerToken) throws MessagingException;
}
