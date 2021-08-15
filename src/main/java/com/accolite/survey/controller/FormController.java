package com.accolite.survey.controller;


import java.io.IOException;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accolite.survey.entity.Form;
import com.accolite.survey.service.FormService;

@RestController
@RequestMapping("/api")
public class FormController{
	@Autowired
	private FormService formService;
	@GetMapping("/form/{id}")
	public Form getFormById(@PathVariable String id){
		return formService.getFormByID(id);
	}
	
	@GetMapping("/formByHr/{createdBy}")
	public List<Form> getAllForm(@PathVariable String createdBy){
		return formService.getAllForm(createdBy);
		//return null;
    
  // Method to submit a Form into a database
	@PostMapping("/addform")
	public boolean addForm(@RequestBody Form form)
	{
		return formService.addForm(form);
	}
	
}
