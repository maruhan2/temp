package com.example.demo.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

public class ResourceNotFoundException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(String message) {
		super(message, null, false, false);	
	}
	
	public ResourceNotFoundException(String message, Throwable throwable) {
		super(message, throwable);	
	}
}

