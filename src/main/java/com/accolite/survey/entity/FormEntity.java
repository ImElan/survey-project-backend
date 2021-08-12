package com.accolite.survey.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("expense")
public class FormEntity {
	
	@Id
	private String formId;
	@Field(name = "description")
	private String formDescription;
	@Field(name = "questions")
	private String questions;
	@Field(name = "creator")
	private String createdby;
	
	public FormEntity(String formId, String formDescription, String questions) {
//		super();
		this.formId = formId;
		this.formDescription = formDescription;
		this.questions = questions;
	}
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}
	public String getFormDescription() {
		return formDescription;
	}
	public void setFormDescription(String formDescription) {
		this.formDescription = formDescription;
	}
	public String getQuestions() {
		return questions;
	}
	public void setQuestions(String questions) {
		this.questions = questions;
	}
	public String getCreatedby() {
		return createdby;
	}
	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}

}
