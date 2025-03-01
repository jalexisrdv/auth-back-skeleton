package com.jardvcode.shared.configuration.errorhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.jardvcode.shared.domain.exception.GeneralServiceException;
import com.jardvcode.shared.domain.exception.NotDataFoundException;
import com.jardvcode.shared.domain.exception.ApplicationException;
import com.jardvcode.shared.domain.response.ResponseWrapper;

@ControllerAdvice
public class ErrorHandlerConfig extends ResponseEntityExceptionHandler {
	
	private final static Logger log = LoggerFactory.getLogger("errorHandlerLog");
	
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException e, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		log.info(e.getMessage(), e);
		String message = "The value " + e.getValue() + " does not match the expected value type.";
		if(e.getPropertyName() != null) {
			message = "The field " + e.getPropertyName() + " with value " + e.getValue() + " does not match the expected value type.";	
		}
		return ResponseWrapper.objectBadRequest(message);
	}

	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.info(e.getMessage(), e);
		BindingResult error = e.getBindingResult();
		String message = error.getFieldError().getDefaultMessage();
		return ResponseWrapper.objectBadRequest(message);
	}

	@ExceptionHandler(GeneralServiceException.class)
    public ResponseEntity<?> handleException(GeneralServiceException e) {
		return ResponseWrapper.internalServerError(e.getMessage());
    }
	
	@ExceptionHandler(NotDataFoundException.class)
    public ResponseEntity<?> handleException(NotDataFoundException e) {
		return ResponseWrapper.notFound(e.getMessage());
    }
	
	@ExceptionHandler(ApplicationException.class)
    public ResponseEntity<?> handleException(ApplicationException e) {
		return ResponseWrapper.badRequest(e.getMessage());
    }
	
}
