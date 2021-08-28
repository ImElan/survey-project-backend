package com.accolite.survey.Exception.AuthException;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

/*
 * 	Handler to handle ApiRequestionException
 * 
 */

@ControllerAdvice
public class AuthExceptionHandler {

	@ExceptionHandler(value = {AuthApiRequestException.class})
	public ResponseEntity<Object> handleApiException(AuthApiRequestException exception) {
		AuthException authException = new AuthException(
			exception.getMessage(),
			HttpStatus.BAD_REQUEST,
			ZonedDateTime.now(ZoneId.of("Asia/Kolkata"))
		);
		return new ResponseEntity<>(authException, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = {MissingRequestHeaderException.class})
	public ResponseEntity<Object> handleAuthorizationException(MissingRequestHeaderException exception) {
		if(exception.getHeaderName().equals("Authorization")) {
			AuthException authException = new AuthException(
				"You're not logged in. Please login to access this route.",
				HttpStatus.BAD_REQUEST,
				ZonedDateTime.now(ZoneId.of("Asia/Kolkata"))
			);
			return new ResponseEntity<>(authException, HttpStatus.BAD_REQUEST);
		}
		AuthException authException = new AuthException(
			"Something went wrong....",
			HttpStatus.BAD_REQUEST,
			ZonedDateTime.now(ZoneId.of("Asia/Kolkata"))
		);
		return new ResponseEntity<>(authException, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = {InvalidFormatException.class})
	public ResponseEntity<Object> handleAuthorizationException(InvalidFormatException exception) {
		AuthException authException = new AuthException(
			"Please select a valid role. [HR, PM, ADMIN, EMPLOYEE]",
			HttpStatus.BAD_REQUEST,
			ZonedDateTime.now(ZoneId.of("Asia/Kolkata"))
		);
		return new ResponseEntity<>(authException, HttpStatus.BAD_REQUEST);
	}
}
	