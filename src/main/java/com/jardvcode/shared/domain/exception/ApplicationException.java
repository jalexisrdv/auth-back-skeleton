package com.jardvcode.shared.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ApplicationException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public ApplicationException(String message) {
		super(message);
	}
	
}
