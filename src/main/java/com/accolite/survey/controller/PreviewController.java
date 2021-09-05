package com.accolite.survey.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;


import com.accolite.survey.entity.Preview;
import com.accolite.survey.service.PreviewService;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class PreviewController {

	@Autowired
	PreviewService previewService ;
	@PostMapping("/post/preview")
	public String postpreview(@RequestBody Preview preview,@RequestHeader("Authorization") String bearerToken){
		
		return previewService.addResponse(preview,bearerToken);
	}
	
      @GetMapping("/get/preview/{id}")
      public Optional<Preview> getpreview(@PathVariable String id, @RequestHeader("Authorization") String bearerToken)
      {
    	  return previewService.getResponse(id,bearerToken);
      }
	
	
}
