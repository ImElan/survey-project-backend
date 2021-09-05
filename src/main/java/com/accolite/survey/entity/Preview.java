package com.accolite.survey.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import org.springframework.data.annotation.Id;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Entity
@Document("Preview")
public class Preview {
	@Id
	private String id;
	@Field("title")
	private String title ;
	@Field("description")
	private String description ;
	@Field("questions")
	private List<PreviewQuestions> questions; 
	@Field("totalQuestions")
	private String totalQuestions ;
	@Field("questionsPerPage")
	private String questionsPerPage ;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<PreviewQuestions> getQuestions() {
		return questions;
	}
	public void setQuestions(List<PreviewQuestions> questions) {
		this.questions = questions;
	}
	public String getTotalQuestions() {
		return totalQuestions;
	}
	public void setTotalQuestions(String totalQuestions) {
		this.totalQuestions = totalQuestions;
	}
	public String getQuestionsPerPage() {
		return questionsPerPage;
	}
	public void setQuestionsPerPage(String questionsPerPage) {
		this.questionsPerPage = questionsPerPage;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
}
