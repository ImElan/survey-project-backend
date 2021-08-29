package com.accolite.survey.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.accolite.survey.entity.Form;

@Service
public interface FormService {	
	public Form getFormByID(String id,String bearertoken);
	public boolean addForm(Form form,String bearertoken);
	public List<Form> getAllForm(String createdBy,String bearertoken);
	public boolean updateForm(Form form,String bearertoken);
}
