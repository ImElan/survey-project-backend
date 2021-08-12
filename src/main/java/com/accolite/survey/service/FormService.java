package com.accolite.survey.service;

import java.util.Optional;

import com.accolite.survey.entity.Form;

public interface FormService {	
	public Optional<Form> getFormByID(String id);
}
