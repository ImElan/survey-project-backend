package com.accolite.survey.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accolite.survey.entity.Responses;
import com.accolite.survey.service.ResponseService;


@RestController
@RequestMapping("/api")
public class ResponseController {
	@Autowired
	ResponseService responseService;
	
//	public ResponseController(ResponseService responseService) {
//		this.responseService = responseService;
//	}
	@PostMapping
	public void addResponse(@RequestBody Responses response) {
		responseService.addResponse(response);
	}
	@GetMapping
	public void add() {
	}
	@GetMapping("/{user_id}/{form_id}")
	public List<Responses> check(@PathVariable int user_id, @PathVariable int form_id) {
		return responseService.check(user_id, form_id);
	}
}

