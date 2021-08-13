package com.accolite.survey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.accolite.survey.entity.QuestionType;
import com.accolite.survey.entity.Responses;
import com.accolite.survey.service.ResponseService;

@SpringBootApplication
@ComponentScan("com.accolite.survey")
public class SurveyApplication implements CommandLineRunner {

	@Autowired
	ResponseService r;

	public static void main(String[] args) {
		SpringApplication.run(SurveyApplication.class, args);
		System.out.println("Hi");
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		//r.addResponse(new Responses(QuestionType.SINGLE,"?????",".....",1,1));
		System.out.println(r.check(1,1));
	}

}
