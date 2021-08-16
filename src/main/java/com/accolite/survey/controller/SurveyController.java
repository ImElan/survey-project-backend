package com.accolite.survey.controller;


import static org.springframework.http.ResponseEntity.created;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

import java.net.URI;
import java.util.List;
import org.glassfish.jersey.client.authentication.ResponseAuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accolite.survey.DAO.SurveyRepository;
import com.accolite.survey.entity.Responses;

@RestController
@RequestMapping("/api")
public class SurveyController {

	@Autowired
	SurveyRepository surveyRepository;

	
	@PostMapping("/validation")
	public boolean Responses (Responses res) {
		List<Responses> data_response = surveyRepository.findAll();
		for(Responses other : data_response) {
			if( other.getUserId().equals(res.getUserId())) {
				if(other.getFormId().equals(res.getFormId()))
				    return false;
			}
		}
		return true;
	}
	
	
	@GetMapping("/allData")
	public List<Responses> getAll(Responses response) {
		try {
			return surveyRepository.findAll(); 
		} catch (ResponseAuthenticationException e) {
			e.getStackTrace();
		}
		return null;
	}
	
	@PostMapping(value="/save")
	public boolean save(@RequestBody Responses response) {
		// to write into database survey
		
		try {
			surveyRepository.save(response);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	//deleting a particular user's response by using id
	@DeleteMapping("/delete/{id}")
	public boolean deleteById(@PathVariable("id") Integer id){
		try {
			surveyRepository.deleteById(id);
		    return true;
		} catch (Exception e) {
			return false;
		}    	
}
	
}