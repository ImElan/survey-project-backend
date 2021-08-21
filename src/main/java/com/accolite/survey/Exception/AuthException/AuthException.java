package com.accolite.survey.Exception.AuthException;

import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;

/*
 * 	Custom exception class to send json response during logical and runtime errors.
 * 
 */
public class AuthException {
	private final String message;
	private final HttpStatus statusCode;
	private final ZonedDateTime dateTime;
	
	public AuthException(String message, HttpStatus statusCode, ZonedDateTime dateTime) {
		this.message = message;
		this.statusCode = statusCode;
		this.dateTime = dateTime;
	}

	public String getMessage() {
		return message;
	}

	public HttpStatus getStatusCode() {
		return statusCode;
	}

	public ZonedDateTime getDateTime() {
		return dateTime;
	}
}
