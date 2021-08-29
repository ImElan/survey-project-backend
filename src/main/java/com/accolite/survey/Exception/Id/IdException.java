package com.accolite.survey.Exception.Id;

import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;

public class IdException {
	private final String message;
	private final HttpStatus statusCode;
	private final ZonedDateTime dateTime;
	
	public IdException(String message, HttpStatus statusCode, ZonedDateTime dateTime) {
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
