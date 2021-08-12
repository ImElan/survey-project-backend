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
	ResponsesService responseService;
	
	@PostMapping
	public void addResponse(@RequestBody Responses response) {
		responseService.addResponse(response);
		System.out.println("loki");
	}
	
	@GetMapping
	public List<Responses> getAllResponses() {
		System.out.println("getMapping worrked");
		return responseService.getAllResponses();
	}
	
	@GetMapping("/{formid}")
    public Responses getResponseByFormId(@PathVariable String formid) {
		System.out.println("Got formid as params");
        return responseService.getResponseByFormId(formid);
    }
}
