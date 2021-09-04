package com.accolite.survey.controller;

import java.util.List;

import javax.mail.MessagingException;

import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accolite.survey.entity.Form;
import com.accolite.survey.entity.Responses;
import com.accolite.survey.service.ResponsesService;
import com.accolite.survey.service.UserService;
@RestController
@RequestMapping("/response")
@CrossOrigin(origins = "http://localhost:3000")
public class ResponsesController {
	
	@Autowired
	ResponsesService responseService ;
	
	@Autowired
	UserService userService ;

	@PostMapping
	public String addResponse(@RequestBody Responses response, @RequestHeader("Authorization") String bearerToken) throws MyException, MessagingException {
		if(response.getUserId()==null || response.getUserId().isBlank()) {
			return "Please provide UserId" ;
		}
		responseService.addResponse(response,bearerToken);
		return "Response Successfully Added" ;
	}
	
	@GetMapping
	public List<Responses> getAllResponses(@RequestHeader("Authorization") String bearerToken) {
		return responseService.getAllResponses(bearerToken);
	}
	
	@GetMapping("/{formid}")
    public List<Responses> getResponseByFormId(@PathVariable String formid,@RequestHeader("Authorization") String bearerToken) throws MyException {
		if(formid==null || formid.isBlank()) {
			throw new MyException("Please provide a valid formId\n") ;
		}
        return responseService.getResponseByFormId(formid,bearerToken) ;
    }
	
	@GetMapping("/{form_id}/{user_id}")
	public ResponseEntity<Responses> check(@PathVariable String user_id, @PathVariable String form_id) throws MyException  {
		return ResponseEntity.ok(responseService.check(user_id, form_id));
	}
	@PutMapping("/updateresponse")
	public Responses updateResponse(@RequestBody Responses responses){
		return responseService.updateResponse(responses);
		//return null;
	}
}
