package com.accolite.survey.Exception.Id;

public class InvalidIdException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public InvalidIdException(String message) {
		super(message);
	}
	
	public InvalidIdException(String message, Throwable cause) {
		super(message, cause);
	}
}
