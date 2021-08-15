  
package com.accolite.survey.entity;

import java.util.List;
import java.util.Objects;

public class SurveyQuestion {
	
	private QuestionType questionType;
	private String question;
	private List<String> options;
	private String paragraph;
	private List<Integer> stars;
	
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
	public String getParagraph() {
		return paragraph;
	}
	public void setParagraph(String paragraph) {
		this.paragraph = paragraph;
	}
	public List<Integer> getStars() {
		return stars;
	}
	public void setStars(List<Integer> stars) {
		this.stars = stars;
	}


		@Override
	public int hashCode() {
		return Objects.hash(question, options, questionType, paragraph, stars);
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
				&& questionType == other.questionType && Objects.equals(paragraph, other.paragraph) 
				&& Objects.equals(stars, other.stars);
	}


	public SurveyQuestion(QuestionType questionType, String question, List<String> options, String paragraph,
			List<Integer> stars) {
		super();
		this.questionType = questionType;
		this.question = question;
		this.options = options;
		this.paragraph = paragraph;
		this.stars = stars;
	}
	@Override
	public String toString() {
		return "SurveyQuestion [questionType=" + questionType + ", Question=" + question + ", options=" + options
				+ ", paragraph=" + paragraph + ", stars=" + stars + "]";
	}
	
	
}