package com.andd.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RequestException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6898275835669421847L;

	public RequestException(Exception e) {
		super(e);
	}

}
