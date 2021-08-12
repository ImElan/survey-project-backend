package com.accolite.survey.entity;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Document("")
public class SurveyQuestion {
	
	public SurveyQuestion(QuestionType questionType, String question, List<String> options) {
		super();
		this.questionType = questionType;
		Question = question;
		this.options = options;
	}
	
	@Field(name="Type")
	private QuestionType questionType;
	@Field(name="QuestionString")
	private String Question;
	@Field(name="Option")
	private List<String> options;
	
}
