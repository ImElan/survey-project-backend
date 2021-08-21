package com.accolite.survey.entity;

import java.util.List;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Forms")
public class Form {
	@Id
	private String id;
	private String formTitle;
	private String formDescription;
	private boolean isEditable;
	private List<SurveyQuestion> surveyQuestions;
	private String createdBy;
	

	public Form(String id, String formTitle, String formDescription, boolean isEditable, List<SurveyQuestion> surveyQuestions,
			String createdBy) {
		super();
		this.id = id;
		this.formTitle = formTitle;
		this.formDescription = formDescription;
		this.isEditable = isEditable;
		this.surveyQuestions = surveyQuestions;
		this.createdBy = createdBy;
		
	}

	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getFormid() {
		return id;
	}

	public void setFormid(String id) {
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

	public boolean isEditable() {
		return isEditable;
	}

	public void setIsEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

	public List<SurveyQuestion> getSurveyQuestions() {
		return surveyQuestions;
	}

	public void setSurveyQuestions(List<SurveyQuestion> surveyQuestions) {
		this.surveyQuestions = surveyQuestions;
	}

	@Override
	public int hashCode() {
		return Objects.hash(createdBy, formDescription, formTitle, id, surveyQuestions);
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
		return Objects.equals(createdBy, other.createdBy) && Objects.equals(formDescription, other.formDescription)
				&& Objects.equals(formTitle, other.formTitle) && Objects.equals(id, other.id)
				 && Objects.equals(surveyQuestions, other.surveyQuestions);
	}

}
