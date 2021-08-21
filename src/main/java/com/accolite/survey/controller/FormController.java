package com.accolite.survey.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accolite.survey.entity.Form;
import com.accolite.survey.service.FormService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class FormController{
	@Autowired
	private FormService formService;
	@GetMapping("/form/{id}")
	public Form getFormById(@PathVariable String id){
		
		return formService.getFormByID(id);
	}
	
	@PostMapping("/addform")
	public boolean addForm(@RequestBody Form form) {
		
		return formService.addForm(form);
	}
	
	@GetMapping("/formbyhr/{createdBy}")
	public List<Form> getAllForm(@PathVariable String createdBy){
		return formService.getAllForm(createdBy);
		//return null;
	}
	
	@PutMapping("/updateform")
	public boolean updateForm(@RequestBody Form form){
		return formService.updateForm(form);
		//return null;
	}
	
}
