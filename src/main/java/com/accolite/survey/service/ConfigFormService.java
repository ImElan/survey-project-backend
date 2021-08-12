package com.accolite.survey.service;


import java.util.List;

import com.accolite.survey.entity.SurveyFormConfig;

public interface ConfigFormService {
	public List<SurveyFormConfig> getAllSurveyFormConfig();
	public SurveyFormConfig getSurveyFormConfig(String id);
	public void addSurveyFormConfig(SurveyFormConfig surveyFormConfig);
	public void updateSurveyFormConfig(SurveyFormConfig surveyFormConfig);
	public void deleteSurveyFormConfig(String id);
	
	
	
}
