package com.accolite.survey.controller;

import java.util.List;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.accolite.survey.entity.Responses;
import com.accolite.survey.service.ResponsesService;

@RestController
@RequestMapping("/response")
public class ResponsesController {
	
	@Autowired
	ResponsesService responseService ;
	
	@PostMapping
	public String addResponse(@RequestBody Responses response) {
		if(response.getUserId()==null || response.getUserId().length()==0) {
			return "Please provide UserId" ;
		}
		responseService.addResponse(response) ;
		return "Responses Successfully Added" ;
	}
	
	@GetMapping
	public List<Responses> getAllResponses() {
		return responseService.getAllResponses();
	}
	
	@GetMapping("/{formid}")
    public Responses getResponseByFormId(@PathVariable String formid) {
		if(formid==null || formid.length()==0) {
			return null ;
		}
        return responseService.getResponseByFormId(formid) ;
    }
}
