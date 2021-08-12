package com.accolite.survey.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accolite.survey.entity.SurveyFormConfig;
import com.accolite.survey.service.ConfigFormService;


@RestController
@RequestMapping("/api/config")
public class ConfigFormController {
	@Autowired
	private ConfigFormService configFormService;
	
	//fetching data using unique id field
	@GetMapping("/{id}")
	public SurveyFormConfig getSurveyFormConfig(@PathVariable String id) {
		SurveyFormConfig surveyConfig = configFormService.getSurveyFormConfig(id);
		if (surveyConfig == null) {
			throw new RuntimeException("No Object found corresponding to the given id..");
		}
		return surveyConfig;
	}
	
	
	@GetMapping
	public List<SurveyFormConfig> getAllSurveyFormConfig() {
		List<SurveyFormConfig> surveyConfig = configFormService.getAllSurveyFormConfig();
		if (surveyConfig == null) {
			throw new RuntimeException("No Data Found in Config Collection..");
		}
		return surveyConfig;
	}
	
	// Inserting the document using post request
	@PostMapping
	public ResponseEntity addSurveyFormConfig(@RequestBody SurveyFormConfig surveyFormConfig) {
		configFormService.addSurveyFormConfig(surveyFormConfig);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	
	// Updating the document
	@PutMapping
	public ResponseEntity updateSurveyFormConfig(@RequestBody SurveyFormConfig surveyFormConfig) {
		configFormService.updateSurveyFormConfig(surveyFormConfig);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	
	// deleting the document using unique id
	@DeleteMapping("/{id}")
    public ResponseEntity deleteSurveyFormConfig(@PathVariable String id) {
        configFormService.deleteSurveyFormConfig(id);
        return ResponseEntity.noContent().build();
    }
	
}
