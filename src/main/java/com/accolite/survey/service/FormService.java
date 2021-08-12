package com.accolite.survey.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.accolite.survey.DAO.FormsRepository;
import com.accolite.survey.entity.Form;

@Service
public class FormService {
	
	private final FormsRepository formRepository;
	
	public FormService(FormsRepository formRepository) {
//		super();
		this.formRepository = formRepository;
	}

	// Method to submit a Form into a database
	public void addForm(Form form) {
		formRepository.insert(form);
	}
	
	// Method to get different Form submit into a database
	public List<Form> getAllForms() {
		return formRepository.findAll();
	}
	
}
