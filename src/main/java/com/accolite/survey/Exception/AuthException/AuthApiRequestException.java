package com.accolite.survey.Exception.AuthException;

public class AuthApiRequestException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public AuthApiRequestException(String message) {
		super(message);
	}
	
	public AuthApiRequestException(String message, Throwable cause) {
		super(message, cause);
	}
}
