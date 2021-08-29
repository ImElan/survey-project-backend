package com.accolite.survey.Exception.Id;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;
import java.time.ZoneId;

@ControllerAdvice
public class IdExceptionHandler {
	@ExceptionHandler(value = {IdNotFoundException.class})
	public ResponseEntity<Object> handleNotFoundException(IdNotFoundException exception) {
		IdException idException = new IdException(
			exception.getMessage(),
			HttpStatus.NOT_FOUND,
			ZonedDateTime.now(ZoneId.of("Asia/Kolkata"))
		);
		return new ResponseEntity<>(idException, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(value = {InvalidIdException.class})
	public ResponseEntity<Object> handleInvalidException(InvalidIdException exception) {
		IdException idException = new IdException(
			exception.getMessage(),
			HttpStatus.BAD_REQUEST,
			ZonedDateTime.now(ZoneId.of("Asia/Kolkata"))
		);
		return new ResponseEntity<>(idException, HttpStatus.BAD_REQUEST);
	}
}
