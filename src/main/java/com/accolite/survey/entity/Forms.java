package com.accolite.survey.entity;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;



@Document(collection = "Forms")
public class Forms {
	
	@Id
	private String id;
	@Field(name="Title")
	private String formTitle;
	@Field(name="Description")
	private String formDescription;
	@Field(name="Questions")
	private List<SurveyQuestion> surveyQuestions;
	
	public Forms(String formTitle, String formDescription, List<SurveyQuestion> surveyQuestion) {
//		super();
		this.formTitle = formTitle;
		this.formDescription = formDescription;
		this.surveyQuestions = surveyQuestion;
//		this.surveyQuestions = ((SurveyQuestion) this.surveyQuestions).bind(this);
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFormTitle() {
		return formTitle;
	}
	public void setFormTitle(String formTitle) {
		this.formTitle = formTitle;
	}
	public String getFormDescription() {
		return formDescription;
	}
	public void setFormDescription(String formDescription) {
		this.formDescription = formDescription;
	}
	public List<SurveyQuestion> getSurveyQuestions() {
		return surveyQuestions;
	}
	public void setSurveyQuestions(List<SurveyQuestion> surveyQuestions) {
		this.surveyQuestions = surveyQuestions;
	}	
	
}
