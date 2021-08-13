package com.accolite.survey.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accolite.survey.DAO.FormDAO;
import com.accolite.survey.entity.Form;

@Service
public class FormServiceImpl implements FormService {

	@Autowired
	public FormDAO formDAO;

	@Override
	public Optional<Form> getFormByID(String id) {
		// TODO Auto-generated method stub
		return formDAO.findById(id);
		
	}

	@Override
	public String addForm(Form form) {
		// TODO Auto-generated method stub
		formDAO.save(form);
		return "Form added successfully";
	}

}
