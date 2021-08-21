package com.accolite.survey.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.accolite.survey.DAO.ResponsesDAO;
import com.accolite.survey.controller.MyException;
import com.accolite.survey.entity.Responses;


@Service
@Transactional
public class ResponsesServiceImplementation implements ResponsesService {
	
	@Autowired
	ResponsesDAO responsedao;
	
	@Autowired
	private MongoTemplate mongoTemplate;

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
	public Responses check(String user_id, String form_id) throws MyException {
		
		Query query = new Query();
		Criteria criteria = new Criteria();
		criteria.and("formid").is(form_id);
		
		if(user_id != null || !user_id.isBlank()) {
			criteria.and("userid").is(user_id);
		}else {
			throw new MyException("User id is not valid");
		}
		
		query.addCriteria(criteria);
		List<Responses> responses = mongoTemplate.find(query, Responses.class);
		
		if(responses == null || responses.isEmpty()) {
			throw new MyException("Response with particular user id is not found");
		}
		else {
		return responses.get(0);
		}
//		List<Responses> ans = getResponseByFormId(form_id);
//		for(int i=0 ; i<ans.size() ; i++) {
//			if(ans.get(i).getUserId().equals(user_id)) {
//				return ans.get(i) ;
//			}
//		}
//		throw new MyException("No response with this userId, formId found\n") ;
	}

}
