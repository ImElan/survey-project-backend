package com.accolite.survey.controller;

import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accolite.survey.entity.Responses;
import com.accolite.survey.service.ResponsesService;
import com.accolite.survey.service.UserService;

@RestController
@RequestMapping("/response")
public class ResponsesController {
	
	@Autowired
	ResponsesService responseService ;
	
	@Autowired
	UserService userService ;
	
	@PostMapping
	public String addResponse(@RequestBody Responses response) throws MyException, MessagingException {
		
		if(response.getUserId()==null || response.getUserId().isBlank()) {
			return "Please provide UserId" ;
		}
		return responseService.addResponse(response);
	}
	
	@GetMapping
	public List<Responses> getAllResponses() {
		return responseService.getAllResponses();
	}
	
	@GetMapping("/{formid}")
    public List<Responses> getResponseByFormId(@PathVariable String formid) throws MyException {
		if(formid==null || formid.isBlank()) {
			throw new MyException("Please provide a valid formId\n") ;
		}
        return responseService.getResponseByFormId(formid) ;
    }
	
	@GetMapping("/{form_id}/{user_id}")
	public ResponseEntity<Responses> check(@PathVariable String user_id, @PathVariable String form_id) throws MyException  {
		return ResponseEntity.ok(responseService.check(user_id, form_id));
	}
	
//	@GetMapping("/send-copy/{form_id}/{user_id}")
//	public String sendResponseCopy(@PathVariable String user_id, @PathVariable String form_id) throws MyException, MessagingException{
//
//		Responses response = responseService.check(user_id, form_id);
//		if(response.getSendCopy() == 0) {
//			return "User doesn't want copy of response";
//		}
//		Sheet copy = responseService.createResponseCopy(response);
//	    return responseService.sendEmailWithAttachment(user_id, copy);
//	}
}
