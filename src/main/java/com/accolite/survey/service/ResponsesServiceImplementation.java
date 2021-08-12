package com.accolite.survey.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accolite.survey.DAO.ResponsesDAO;
import com.accolite.survey.entity.Responses;

@Service
@Transactional
public class ResponsesServiceImplementation implements ResponsesService {
	
	@Autowired
	ResponsesDAO responsedao;

//	@Override
//	public Responses getById(String id) {
//		// TODO Auto-generated method stub
//		Responses responses = responsedao.findById(id)
//				.orElseThrow(() -> new RuntimeException(String.format("Cannot Find ResponseObject by Given ID %s")));
//		return responses;
//	}

	@Override
	public Responses addResponse(Responses response) {
		return responsedao.insert(response);
	}

	@Override
	public List<Responses> getAllResponses() {
		return responsedao.findAll();
	}

	@Override
	public Responses getResponseByFormId(String formid) {
		Responses responses = responsedao.findByFormId(formid).orElseThrow(() -> 
		new RuntimeException(String.format("Cannot Find ResponseObject by Given ID %s")));
		
		return responses;
	}

}
