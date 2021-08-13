package com.accolite.survey.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import com.accolite.survey.DAO.ResponseDAO;
import com.accolite.survey.entity.Responses;

@Service
public class ResponseService {
	@Autowired
	ResponseDAO responseRepository;

//	public ResponseService(ResponseRepository responseRepository) {
//		this.responseRepository = responseRepository;
//	}

	public void addResponse(Responses response) {
		responseRepository.insert(response);
	}

	public List<Responses> check(int user_id, int form_id) {
		Responses checkUser = new Responses();
		checkUser.setUserId(user_id);
		checkUser.setFormId(form_id);
		ExampleMatcher exampleMatcher = ExampleMatcher.matching()
				.withMatcher("userId", ExampleMatcher.GenericPropertyMatchers.exact())
				.withMatcher("formId", ExampleMatcher.GenericPropertyMatchers.exact())
				.withIgnorePaths("questionType", "question", "answer");
		Example<Responses> example = Example.of(checkUser, exampleMatcher);
		return responseRepository.findAll(example);
	}
}

