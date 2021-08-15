package com.accolite.survey.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.accolite.survey.controller.MyException;
import com.accolite.survey.entity.Responses;

public interface ResponsesService {

	public Responses addResponse(Responses response) throws MyException;

	public List<Responses> getAllResponses();

	public List<Responses> getResponseByFormId(String formid);
	
}
