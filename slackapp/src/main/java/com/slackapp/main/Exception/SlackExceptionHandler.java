package com.slackapp.main.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SlackExceptionHandler {
	
	@ExceptionHandler(EmailException.class)
	public ResponseEntity<?> emailException(EmailException emailException){
		return new ResponseEntity<>("Email Not send",HttpStatus.BAD_REQUEST);
	}

}
