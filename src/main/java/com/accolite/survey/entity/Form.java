package com.accolite.survey.entity;
import java.util.List;
import java.util.Objects;


import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Forms")
public class Form {
	
	private String formTitle;
	private String formDescription;
	private List<SurveyQuestion> surveyQuestions;
	private int filledby;
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
	public int getFilledby() {
		return filledby;
	}
	public void setFilledby(int filledby) {
		this.filledby = filledby;
	}
	@Override
	public int hashCode() {
		return Objects.hash(filledby, formDescription, formTitle, surveyQuestions);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Form other = (Form) obj;
		return filledby == other.filledby && Objects.equals(formDescription, other.formDescription)
				&& Objects.equals(formTitle, other.formTitle) && Objects.equals(surveyQuestions, other.surveyQuestions);
	}
	@Override
	public String toString() {
		return "Forms [formTitle=" + formTitle + ", formDescription=" + formDescription + ", surveyQuestions="
				+ surveyQuestions + ", filledby=" + filledby + "]";
	}
	public Form(String formTitle, String formDescription, List<SurveyQuestion> surveyQuestions, int filledby) {
		super();
		this.formTitle = formTitle;
		this.formDescription = formDescription;
		this.surveyQuestions = surveyQuestions;
		this.filledby = filledby;
	}
	
	
	
	
}
