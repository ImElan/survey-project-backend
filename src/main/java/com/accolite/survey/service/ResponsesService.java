package com.accolite.survey.service;

import java.util.List;

import javax.mail.MessagingException;

import org.apache.poi.ss.usermodel.Sheet;

import com.accolite.survey.controller.MyException;
import com.accolite.survey.entity.Responses;

public interface ResponsesService {

	public Responses addResponse(Responses response) throws MyException;

	public List<Responses> getAllResponses();

	public List<Responses> getResponseByFormId(String formid);
	
	public Responses check(String user_id, String form_id) throws MyException;
	
	public Sheet createResponseCopy(Responses response);
	
	public String sendEmailWithAttachment(String toEmail, Sheet attachment) throws MessagingException;
}
