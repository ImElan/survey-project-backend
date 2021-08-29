package com.accolite.survey.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.accolite.survey.DAO.FormDAO;
import com.accolite.survey.DAO.Auth.AuthDAOImplementation;
import com.accolite.survey.Model.TokenType;
import com.accolite.survey.entity.Form;
import com.accolite.survey.entity.User;
import com.accolite.survey.entity.UserRoles;

@Service
public class FormServiceImpl implements FormService {
	@Autowired
	 AuthDAOImplementation authdao;
	
	public FormDAO formDAO;
	@Autowired
	public FormServiceImpl(FormDAO formDAO) {
		super();
		this.formDAO = formDAO;
	}

	@Override
	public Form getFormByID(String id,String bearerToken) {
		// TODO Auto-generated method stub
		User user = authdao.isAuthenticated(bearerToken,TokenType.ACCESS);
		UserRoles[] roles = {UserRoles.HR};
		authdao.restrictTo(roles, user);
		return formDAO.findById(id)
				.orElseThrow(() -> new RuntimeException(String.format("Can't find any form by id %s",id)));		
	}

	@Override
	public  boolean addForm(Form form,String bearerToken) {
		User user = authdao.isAuthenticated(bearerToken,TokenType.ACCESS);
		UserRoles[] roles = {UserRoles.HR};
		authdao.restrictTo(roles, user);
		form.setCreatedBy(user.getName());
		formDAO.save(form);
		return true;
	}

	@Override
	public List<Form> getAllForm(String createdBy,String bearerToken) {
		// TODO Auto-generated method stub
		User user = authdao.isAuthenticated(bearerToken,TokenType.ACCESS);
		UserRoles[] roles = {UserRoles.HR};
		authdao.restrictTo(roles, user);
		return formDAO.getAllForm(createdBy);
	}

	@Override
	public boolean updateForm(Form form,String bearerToken) {
		User user = authdao.isAuthenticated(bearerToken,TokenType.ACCESS);
		UserRoles[] roles = {UserRoles.HR};
		authdao.restrictTo(roles, user);
		Form savedForm = formDAO.findById(form.getId())
				.orElseThrow(() -> new RuntimeException(String.format("Can't find any form by id %s",form.getId())));
		savedForm.setFormDescription(form.getFormDescription());
		savedForm.setFormTitle(form.getFormTitle());
		savedForm.setSurveyQuestions(form.getSurveyQuestions());
		formDAO.save(savedForm);
		return true;
	}

}
