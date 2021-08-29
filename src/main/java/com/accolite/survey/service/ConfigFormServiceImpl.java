package com.accolite.survey.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.accolite.survey.DAO.ConfigFormDAO;
import com.accolite.survey.entity.SurveyFormConfig;

@Service
public class ConfigFormServiceImpl implements ConfigFormService{
	
	@Autowired
	private ConfigFormDAO configFormDAO;
	

	@Override
	public List<SurveyFormConfig> getAllSurveyFormConfig() throws Exception {
		List<SurveyFormConfig> allConfig = new ArrayList<>(); 
		try {
			allConfig = configFormDAO.findAll();
		} catch(Exception e) {
			throw new Exception(e);
		}
		return allConfig;
	}

	@Override
	public void addSurveyFormConfig(SurveyFormConfig surveyFormConfig) {
		try {
			configFormDAO.insert(surveyFormConfig);
		}
		catch(Exception e) {
			System.err.println("Unable to insert into database " + e.getMessage());
		}
	}


	@Override
	public SurveyFormConfig getSurveyFormConfig(String id) {
		SurveyFormConfig savedSurveyFormConfig = configFormDAO.findById(id)
				.orElseThrow(() -> new RuntimeException(String.format("Cannot Find ConfigObject by ID %s",id)));
		return savedSurveyFormConfig;
	}

	

}