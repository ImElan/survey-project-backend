package com.accolite.survey.entity;

import java.util.List;

public class PreviewQuestions {
 private String questionId;
 private String question;
 private QuestionType questionType;
 private boolean required;
 private boolean isHalfStarAllowed;
 private boolean isValid;
 private String numStars;
 private String image;
 private String imageData;
 private PreviewOptions options;
public String getQuestionId() {
	return questionId;
}
public void setQuestionId(String questionId) {
	this.questionId = questionId;
}
public String getQuestion() {
	return question;
}
public void setQuestion(String question) {
	this.question = question;
}
public QuestionType getQuestionType() {
	return questionType;
}
public void setQuestionType(QuestionType questionType) {
	this.questionType = questionType;
}
public boolean isRequired() {
	return required;
}
public void setRequired(boolean required) {
	this.required = required;
}
public boolean isHalfStarAllowed() {
	return isHalfStarAllowed;
}
public void setHalfStarAllowed(boolean isHalfStarAllowed) {
	this.isHalfStarAllowed = isHalfStarAllowed;
}
public boolean isValid() {
	return isValid;
}
public void setValid(boolean isValid) {
	this.isValid = isValid;
}
public String getNumStars() {
	return numStars;
}
public void setNumStars(String numStars) {
	this.numStars = numStars;
}
public String getImage() {
	return image;
}
public void setImage(String image) {
	this.image = image;
}
public String getImageData() {
	return imageData;
}
public void setImageData(String imageData) {
	this.imageData = imageData;
}
public PreviewOptions getOptions() {
	return options;
}
public void setOptions(PreviewOptions options) {
	this.options = options;
}

 
}
