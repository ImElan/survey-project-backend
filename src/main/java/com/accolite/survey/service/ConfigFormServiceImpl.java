package com.accolite.survey.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accolite.survey.DAO.ConfigFormDAO;
import com.accolite.survey.entity.SurveyFormConfig;

@Service
public class ConfigFormServiceImpl implements ConfigFormService{
	
	@Autowired
	private ConfigFormDAO configFormDAO;
	

	@Override
	public List<SurveyFormConfig> getAllSurveyFormConfig() {
		return configFormDAO.findAll();
	}

	@Override
	public void addSurveyFormConfig(SurveyFormConfig surveyFormConfig) {
		configFormDAO.insert(surveyFormConfig);
	}

	@Override
	public void updateSurveyFormConfig(SurveyFormConfig surveyFormConfig) {
		
		SurveyFormConfig savedSurveyFormConfig = configFormDAO.findById(surveyFormConfig.getId())
				.orElseThrow(() -> new RuntimeException(String.format("Cannot Find ConfigObject by ID %s")));
		savedSurveyFormConfig.setId(surveyFormConfig.getId());
		savedSurveyFormConfig.setDaysAfterWhichEmailShouldBeSent(surveyFormConfig.getDaysAfterWhichEmailShouldBeSent());
        savedSurveyFormConfig.setMinNUmberOfQuestionsAllowed(surveyFormConfig.getMinNUmberOfQuestionsAllowed());
        savedSurveyFormConfig.setMaxNumberOfQuestionsAllowed(surveyFormConfig.getMaxNumberOfQuestionsAllowed());
        savedSurveyFormConfig.setRemindAfterNumberOfDays(surveyFormConfig.getRemindAfterNumberOfDays());
 
        configFormDAO.save(surveyFormConfig);
	}

	
	@Override
	public void deleteSurveyFormConfig(String id) {
		configFormDAO.deleteById(id);
	}

	@Override
	public SurveyFormConfig getSurveyFormConfig(String id) {
		SurveyFormConfig savedSurveyFormConfig = configFormDAO.findById(id)
				.orElseThrow(() -> new RuntimeException(String.format("Cannot Find ConfigObject by ID %s")));
		return savedSurveyFormConfig;
	}

	

}
