package com.accolite.survey.entity;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;

@Document("Responses")
public class Responses {

	@Id
	private String id ;
	@Field("formid")
	private String formId ;
	@Field("userid")
	private String userId ;
	@Field("questiontype")
	private ArrayList<QuestionType> questiontypes ;
	@Field("questions")
	private ArrayList<String> questions ;
	@Field("answers")
	private ArrayList<String> answers ;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public ArrayList<QuestionType> getQuestiontype() {
		return questiontypes;
	}
	public void setQuestiontype(ArrayList<QuestionType> questiontypes) {
		this.questiontypes = questiontypes;
	}
	public ArrayList<String> getQuestions() {
		return questions;
	}
	public void setQuestions(ArrayList<String> questions) {
		this.questions = questions;
	}
	public ArrayList<String> getAnswers() {
		return answers;
	}
	public void setAnswers(ArrayList<String> answers) {
		this.answers = answers;
	}
	
} 
