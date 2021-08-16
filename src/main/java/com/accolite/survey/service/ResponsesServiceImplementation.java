package com.accolite.survey.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.ExampleMatcher;
import com.accolite.survey.DAO.ResponsesDAO;
import com.accolite.survey.controller.MyException;
import com.accolite.survey.entity.Responses;


@Service
@Transactional
public class ResponsesServiceImplementation implements ResponsesService {
	
	@Autowired
	ResponsesDAO responsedao;

	@Override
	public Responses addResponse(Responses response) throws MyException {
		List<Responses> ans = getResponseByFormId(response.getFormId());
		for(int i=0 ; i<ans.size() ; i++) {
			if(ans.get(i).getUserId().equals(response.getUserId())) {
				throw new MyException("This user can't fill this form as it's been already filled by this userId\n") ;
			}
		}
		return responsedao.insert(response);
	}

	@Override
	public List<Responses> getAllResponses() {
		return responsedao.findAll();
	}

	@Override
	public List<Responses> getResponseByFormId(String formid) {
		List<Responses> response = responsedao.findAll();
		List<Responses> ans = new ArrayList<>();
		for(int i=0 ; i<response.size() ; i++) {
			if(response.get(i).getFormId().equals(formid)) {
				ans.add(response.get(i));
			}
		}
		return ans;
	}

	@Override
	public boolean checkResponse(Responses response) throws MyException{
		if(response.getUserId().isBlank()) {
			throw new MyException("Please provide a valid UserId\n") ;
		}else if(response.getFormId().isBlank()) {
			throw new MyException("Please provide a valid formId\n") ;
		}else if(response.getQuestiontype().size()<=2) {
			throw new MyException("Number of questionType can not be less than 2\n") ;
		}else if(response.getQuestions().size()<=2) {
			throw new MyException("Number of Questions can not be less than 2\n") ;
		}else if(response.getAnswers().size()<=2) {
			throw new MyException("Number of Answers can not be less than 2\n") ;
		}else {
			for(int i=0 ; i<response.getAnswers().size() ; i++) {
				if(response.getAnswers().get(i).isBlank()) {
					throw new MyException("Please provide a valid formId\n") ;
				}
			}
		}
		for(int i=0 ; i<response.getFormId().length(); i++) {
			if(!Character.isDigit(response.getFormId().charAt(i))) {
				throw new MyException("formid must be a number\n") ;
			}
		}
		return true ;
	}

	@Override
	public Responses check(String user_id, String form_id) throws MyException {
		List<Responses> ans = getResponseByFormId(form_id);
		for(int i=0 ; i<ans.size() ; i++) {
			if(ans.get(i).getUserId().equals(user_id)) {
				return ans.get(i) ;
			}
		}
		throw new MyException("No response with this userId, formId found\n") ;
	}

}
