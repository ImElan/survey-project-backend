package com.accolite.survey.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.accolite.survey.DAO.FormDAO;
import com.accolite.survey.entity.Form;

public class FormServiceImpl implements FormService{
	
	@Autowired
	private FormDAO formDAO; 

	@Override
	public Optional<Form> getFormByID(String id) {
		// TODO Auto-generated method stub
		return formDAO.getFormByID(id);
	}

}
