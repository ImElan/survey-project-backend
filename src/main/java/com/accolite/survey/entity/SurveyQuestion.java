  
package com.accolite.survey.entity;

import java.util.List;
import java.util.Objects;

public class SurveyQuestion {
	
	private QuestionType questionType;
	private String question;
	private List<String> options;
	private String numStars;
	private boolean isHalfStarAllowed;
	
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


	public String getNumStars() {
		return numStars;
	}
	public void setNumStars(String numStars) {
		this.numStars = numStars;
	}
	public boolean isHalfStarAllowed() {
		return isHalfStarAllowed;
	}
	public void setHalfStarAllowed(boolean isHalfStarAllowed) {
		this.isHalfStarAllowed = isHalfStarAllowed;
	}
		@Override
	public int hashCode() {
		return Objects.hash(question, options, questionType, numStars, isHalfStarAllowed);
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
		return Objects.equals(question, other.question) && Objects.equals(options, other.options)
				&& questionType == other.questionType && Objects.equals(numStars, other.numStars) 
				&& isHalfStarAllowed == other.isHalfStarAllowed;
	}


	@Override
	public String toString() {
		return "SurveyQuestion [questionType=" + questionType + ", question=" + question + ", options=" + options
				+ ", numStars=" + numStars + ", isHalfStarAllowed=" + isHalfStarAllowed + "]";
	}
	public SurveyQuestion(QuestionType questionType, String question, List<String> options, String numStars,
			boolean isHalfStarAllowed) {
		super();
		this.questionType = questionType;
		this.question = question;
		this.options = options;
		this.numStars = numStars;
		this.isHalfStarAllowed = isHalfStarAllowed;
	}
	
	
	
}