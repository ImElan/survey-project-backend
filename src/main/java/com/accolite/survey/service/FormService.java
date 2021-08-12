package com.accolite.survey.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.accolite.survey.DAO.FormsRepository;
//import com.accolite.survey.entity.FormEntity;
import com.accolite.survey.entity.Forms;

@Service
public class FormService {
	
	private final FormsRepository formRepository;
	
	public FormService(FormsRepository formRepository) {
//		super();
		this.formRepository = formRepository;
	}

	// Method to submit a Form into a database
	public void addForm(Forms form) {
		formRepository.insert(form);
	}
	
	// Method to get different Form submit into a database
	public List<Forms> getAllForms() {
		return formRepository.findAll();
	}
	
}
