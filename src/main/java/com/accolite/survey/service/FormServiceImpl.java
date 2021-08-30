package com.accolite.survey.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.accolite.survey.DAO.FormDAO;
import com.accolite.survey.Exception.Id.IdNotFoundException;
import com.accolite.survey.entity.Form;
import com.accolite.survey.entity.Responses;

@Service
public class FormServiceImpl implements FormService {

	
	public FormDAO formDAO;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	public FormServiceImpl(FormDAO formDAO) {
		super();
		this.formDAO = formDAO;
	}

	@Override
	public Form getFormByID(String id) {
		Query query = new Query();
		Criteria criteria = new Criteria();
		criteria.and("id").is(id);
		query.addCriteria(criteria);
		
		List<Form> form = mongoTemplate.find(query, Form.class);
		
		if(form == null || form.isEmpty()) {
			
			throw new IdNotFoundException("form with particular id is not found");
			
		}
		return form.get(0);	
	}

	@Override
	public  boolean addForm(Form form) {
		formDAO.save(form);
		return true;
	}

	@Override
	public List<Form> getAllForm(String createdBy) {
		// TODO Auto-generated method stub		
		return formDAO.getAllForm(createdBy);
	}

	@Override
	public boolean updateForm(Form form) {
		Form savedForm = formDAO.findById(form.getId())
				.orElseThrow(() -> new RuntimeException(String.format("Can't find any form by id %s",form.getId())));
		savedForm.setFormDescription(form.getFormDescription());
		savedForm.setFormTitle(form.getFormTitle());
		savedForm.setSurveyQuestions(form.getSurveyQuestions());
		formDAO.save(savedForm);
		return true;
	}

}
