package com.accolite.survey.DAO;

import java.util.Optional;

import com.accolite.survey.entity.Form;

public interface FormDAO {
	public Optional<Form> getFormByID(String id);
}
