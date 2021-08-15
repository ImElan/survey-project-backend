package com.accolite.survey.controller;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accolite.survey.entity.Form;
import com.accolite.survey.service.FormService;

@RestController
@RequestMapping("api/form")
public class FormController {
	
	private final FormService formService;
	
	
	public FormController(FormService formService) {
//		super();
		this.formService = formService;
	}


	// Method to submit a Form into a database
	@PostMapping
	public boolean addForm(@RequestBody Form form)
	{
		return formService.addForm(form);
//		return ResponseEntity.status(HttpStatus.CREATED).build();
//		return "Form is inserted";
	}
	
	
	@GetMapping
	public List<Form> getAllForms() throws JsonGenerationException, JsonMappingException, IOException {
		return formService.getAllForms();
	}
	
}
