package com.accolite.survey.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accolite.survey.DAO.SurveyRepository;
import com.accolite.survey.entity.demo_data;



@RestController
@RequestMapping("/api")
public class SurveyController {

	@Autowired
	SurveyRepository surveyRepository;
    
	
	@GetMapping("/all")
	public List<demo_data> getallResponses(){
		//will return responses from database
		return surveyRepository.findAll();
	}
	
	@PostMapping(value="/write")
	public demo_data save(@RequestBody demo_data response) {
		System.out.println("printing....");
		// to write into database survey
		
		demo_data insertedresponse= surveyRepository.insert(response);
		return insertedresponse;
	}
	
	@DeleteMapping("/delete/{id}")
	public void deleteById(@PathVariable("id") String id){
	    surveyRepository.deleteById(id);
	
}

	
}