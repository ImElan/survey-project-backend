package com.accolite.survey.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.accolite.survey.entity.Responses;

public interface ResponsesService {

	public Responses addResponse(Responses response);

	public List<Responses> getAllResponses();

	public Responses getResponseByFormId(String formid);
	
}
