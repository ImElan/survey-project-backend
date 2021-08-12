package com.accolite.survey.entity;

import java.util.List;
import java.util.Objects;

public class SurveyQuestion {
	
	private QuestionType questionType;
	private String Question;
	private List<String> options;
	public QuestionType getQuestionType() {
		return questionType;
	}
	public void setQuestionType(QuestionType questionType) {
		this.questionType = questionType;
	}
	public String getQuestion() {
		return Question;
	}
	public void setQuestion(String question) {
		Question = question;
	}
	public List<String> getOptions() {
		return options;
	}
	public void setOptions(List<String> options) {
		this.options = options;
	}
	@Override
	public int hashCode() {
		return Objects.hash(Question, options, questionType);
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
		return Objects.equals(Question, other.Question) && Objects.equals(options, other.options)
				&& questionType == other.questionType;
	}
	public SurveyQuestion(QuestionType questionType, String question, List<String> options) {
		super();
		this.questionType = questionType;
		Question = question;
		this.options = options;
	}
	@Override
	public String toString() {
		return "SurveyQuestion [questionType=" + questionType + ", Question=" + Question + ", options=" + options + "]";
	}
	
	
}
