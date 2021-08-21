package com.accolite.survey.Exception.Id;

public class IdNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public IdNotFoundException(String message) {
		super(message);
	}
	
	public IdNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
