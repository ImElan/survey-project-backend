package com.accolite.survey.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.accolite.survey.entity.SurveyFormConfig;
import com.accolite.survey.service.ConfigFormService;


@RestController
@RequestMapping("/api/config")
public class ConfigFormController {
	@Autowired
	private ConfigFormService configFormService;
	
	//fetching data using unique id field
	@GetMapping("/{id}")
	public ResponseEntity<SurveyFormConfig> getSurveyFormConfig(@PathVariable String id) {
		SurveyFormConfig surveyFormConfig = null;
		try {
			if (id == null) {
				throw new RuntimeException("Id can not be null");
			}
			surveyFormConfig = configFormService.getSurveyFormConfig(id);
			return new ResponseEntity<>(surveyFormConfig,HttpStatus.OK);
		}
		catch(RuntimeException e) {
			System.err.println(e.getMessage());
			return new ResponseEntity<>(surveyFormConfig,HttpStatus.NOT_FOUND);
		}
		
	}
	
	
	@GetMapping
	public ResponseEntity<List<SurveyFormConfig>> getAllSurveyFormConfig() {
		List<SurveyFormConfig> configList = new ArrayList<>();
		try{
			configList = configFormService.getAllSurveyFormConfig();
			return new ResponseEntity<>(configList, HttpStatus.OK);
		} 
		catch(Exception e) {
			System.err.println(e.getMessage());
			return new ResponseEntity<>(configList, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// Inserting the document using post request
	@PostMapping("/add")
	public ResponseEntity<SurveyFormConfig> addSurveyFormConfig(@Valid @RequestBody SurveyFormConfig surveyFormConfig) {
		
		
		List<SurveyFormConfig> configList = new ArrayList<>();
		SurveyFormConfig surveyConfig = null;
		try{
			configList = configFormService.getAllSurveyFormConfig();
			if (configList.size() > 0) {
		        return new ResponseEntity<>(surveyConfig,HttpStatus.METHOD_NOT_ALLOWED);
			}
			else {
				if (surveyFormConfig.getId() == null) {
					
					System.err.println("Please enter correct inputs in all the fields");
			        return new ResponseEntity<>(surveyConfig,HttpStatus.PARTIAL_CONTENT);
			        
				} else {
					configFormService.addSurveyFormConfig(surveyFormConfig);
					return ResponseEntity.status(HttpStatus.CREATED).build();
				}
				
			}
		} catch(Exception e) {
			System.err.println(e.getMessage());
			return new ResponseEntity<>(surveyConfig,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	
}


@ControllerAdvice
class CustomExceptionHandler extends ResponseEntityExceptionHandler 
{
	 	@Override
	    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
	        SurveyFormConfig surveyConfig = null;
	        for(ObjectError error : ex.getBindingResult().getAllErrors()) {
	            System.err.println(error.getDefaultMessage());
	        }
	        return new ResponseEntity<>(surveyConfig, HttpStatus.BAD_REQUEST);
	    }
	
}


