package com.accolite.survey.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.accolite.survey.DAO.FormDAO;
import com.accolite.survey.entity.Form;

@Service
public class FormServiceImpl implements FormService {

	
	public FormDAO formDAO;
	@Autowired
	public FormServiceImpl(FormDAO formDAO) {
		super();
		this.formDAO = formDAO;
	}

	@Override
	public Form getFormByID(String id) {
		// TODO Auto-generated method stub
		return formDAO.findById(id)
				.orElseThrow(() -> new RuntimeException(String.format("Can't find any form by id %s",id)));		
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

}
