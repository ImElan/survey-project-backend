package com.accolite.survey.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.accolite.survey.entity.Form;
import com.accolite.survey.service.FormService;

@RestController
@RequestMapping("/api")
public class FormController{
	@Autowired
	private FormService formService;
	@GetMapping("/form/{id}")
	public Optional<Form> getFormById(@PathVariable String id){
		return formService.getFormByID(id);
	}
	@PostMapping("/addform")
	public String addForm(@RequestBody Form form) {
		return formService.addForm(form);
	}
	
}
