package com.accolite.survey.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
