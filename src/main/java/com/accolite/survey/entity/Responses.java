package com.accolite.survey.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("response")
public class Responses {
	@Id
	String id;
	@Field (name = "questionType")
	QuestionType questionType;
	@Field(name = "question")
	String question;
	@Field(name = "answer")
	String answer;
	@Field(name = "userId")
	int userId;
	@Field(name = "formId")
	int formId;

	public Responses() {
	}

	public Responses(QuestionType questionType, String question, String answer, int userId, int formId) {
		this.questionType = questionType;
		this.question = question;
		this.answer = answer;
		this.userId = userId;
		this.formId = formId;
	}
	
	public QuestionType getQuestionType() {
		return questionType;
	}

	public void setQuestionType(QuestionType questionType) {
		this.questionType = questionType;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getFormId() {
		return formId;
	}

	public void setFormId(int formId) {
		this.formId = formId;
	}

	public String toString() {
		return "Response: " + questionType + "," + question + ", " + answer + ", " + userId + ", " + formId;
	}
}
