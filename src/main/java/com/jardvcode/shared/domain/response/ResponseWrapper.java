package com.jardvcode.shared.domain.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseWrapper<T> {
	
	private boolean ok;
	private String message;
	private T body;
	
	public ResponseWrapper(boolean ok, T body, String message) {
		this.ok = ok;
		this.message = message;
		this.body = body;
	}
	
	public static <T> ResponseEntity<ResponseWrapper<T>> ok(T data) {
		ResponseWrapper<T> response = new ResponseWrapper<>(true, data, "Successful execution");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
	
	public static <T> ResponseEntity<ResponseWrapper<T>> ok(T data, String message) {
		ResponseWrapper<T> response = new ResponseWrapper<>(true, data, message);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
	
	public static <T> ResponseEntity<ResponseWrapper<T>> badRequest(String message) {
		ResponseWrapper<T> response = new ResponseWrapper<>(false, null, message);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
	
	public static ResponseEntity<Object> objectBadRequest(String message) {
		ResponseWrapper<Object> response = new ResponseWrapper<>(false, null, message);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
	
	public static <T> ResponseEntity<ResponseWrapper<T>> internalServerError(String message) {
		ResponseWrapper<T> response = new ResponseWrapper<>(false, null, message);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
	
	public static <T> ResponseEntity<ResponseWrapper<T>> notFound(String message) {
		ResponseWrapper<T> response = new ResponseWrapper<>(false, null, message);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}

}
