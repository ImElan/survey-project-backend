package com.accolite.survey.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
	public Optional<Form> getFormById(String id){
		
	}
}
