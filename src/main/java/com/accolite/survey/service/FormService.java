package com.accolite.survey.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.accolite.survey.entity.Form;

@Service
public interface FormService {	
	public Optional<Form> getFormByID(String id);
	public String addForm(Form form);
}
