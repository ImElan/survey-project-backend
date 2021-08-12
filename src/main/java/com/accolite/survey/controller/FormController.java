package com.accolite.survey.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import com.accolite.survey.entity.FormEntity;
import com.accolite.survey.entity.Forms;
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
	public ResponseEntity addForm(@RequestBody Forms form)
	{
		formService.addForm(form);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	// Method to get different Form submit into a database
	@GetMapping
	public ResponseEntity<List<Forms>> getAllForms() {
		return ResponseEntity.ok(formService.getAllForms());
	}

}
