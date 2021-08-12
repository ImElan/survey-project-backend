package com.accolite.survey.DAO;



import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.accolite.survey.entity.Form;
import com.accolite.survey.entity.respository.FormRepository;

public class FormDAOImpl implements FormDAO {
	
	@Autowired
	private FormRepository formRepository;

	@Override
	public Optional<Form> getFormByID(String id) {
		// TODO Auto-generated method stub
		return formRepository.findById(id);
	}

}
