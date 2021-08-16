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
	public String addResponse(@RequestBody Responses response) throws MyException {
		if(responseService.checkResponse(response)) {
			responseService.addResponse(response) ;
			return "Responses Successfully Added" ;
		}
		return "Please check your response before submitting the form\n" ;
	}
	
	@GetMapping
	public List<Responses> getAllResponses() throws MyException{
		List<Responses> list = responseService.getAllResponses();
		if(list.size()==0) {
			throw new MyException("No responses yet, fill a form first\n") ;
		}
		return list ;
	}
	
	@GetMapping("/{formid}")
    public List<Responses> getResponseByFormId(@PathVariable String formid) throws MyException {
		if(formid==null || formid.isBlank()) {
			throw new MyException("Please provide a valid formId\n") ;
		}
        return responseService.getResponseByFormId(formid) ;
    }
	
	@GetMapping("/{user_id}/{form_id}")
	public Responses check(@PathVariable String user_id, @PathVariable String form_id) throws MyException  {
		return responseService.check(user_id, form_id);
	}

}
