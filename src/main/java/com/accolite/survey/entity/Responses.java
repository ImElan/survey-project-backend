package com.accolite.survey.entity;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.annotation.Id;
import java.util.ArrayList;
import javax.persistence.Entity;


@Entity
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
	@Field("sendcopy")
	private int sendCopy;
	
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
	public ArrayList<QuestionType> getQuestiontypes() {
		return questiontypes;
	}
	public void setQuestiontypes(ArrayList<QuestionType> questiontypes) {
		this.questiontypes = questiontypes;
	}
	@Override
	public String toString() {
		return "Responses [id=" + id + ", formId=" + formId + ", userId=" + userId + ", questiontypes=" + questiontypes
				+ ", questions=" + questions + ", answers=" + answers + ", sendCopy=" + sendCopy + "]";
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
	public int getSendCopy() {
		return sendCopy;
	}
	public void setSendCopy(int sendCopy) {
		this.sendCopy = sendCopy;
	}
		
} 
