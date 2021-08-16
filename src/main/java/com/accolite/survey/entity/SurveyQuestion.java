package com.accolite.survey.entity;

import java.util.List;
import java.util.Objects;

public class SurveyQuestion {

	private QuestionType questionType;
	private String question;
	private List<String> options;
	private String noOfStars;
	private boolean isHalfStarAllowed;
	private boolean isRequired;

	public SurveyQuestion(QuestionType questionType, String question, List<String> options, String noOfStars,
			boolean isHalfStarAllowed, boolean isRequired) {
		super();
		this.questionType = questionType;
		this.question = question;
		this.options = options;
		this.noOfStars = noOfStars;
		this.isHalfStarAllowed = isHalfStarAllowed;
		this.isRequired = isRequired;
	}

	public boolean isRequired() {
		return isRequired;
	}

	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
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

	public List<String> getOptions() {
		return options;
	}

	public void setOptions(List<String> options) {
		this.options = options;
	}

	public String getNoOfStars() {
		return noOfStars;
	}

	public void setNoOfStars(String noOfStars) {
		this.noOfStars = noOfStars;
	}

	public boolean isHalfStarAllowed() {
		return isHalfStarAllowed;
	}

	public void setHalfStarAllowed(boolean isHalfStarAllowed) {
		this.isHalfStarAllowed = isHalfStarAllowed;
	}

	@Override
	public int hashCode() {
		return Objects.hash(isHalfStarAllowed, isRequired, noOfStars, options, question, questionType);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SurveyQuestion other = (SurveyQuestion) obj;
		return isHalfStarAllowed == other.isHalfStarAllowed && isRequired == other.isRequired
				&& Objects.equals(noOfStars, other.noOfStars) && Objects.equals(options, other.options)
				&& Objects.equals(question, other.question) && questionType == other.questionType;
	}

	

}
