/*
 * Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
 * 
 * Read LICENCE file in project root for licence terms of this software.
 */
package com.github.francescojo.appdeploy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Francesco Jo
 * @since 21 - Dec - 2014
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnexpectedInputException extends RuntimeException {
	private static final long serialVersionUID = 3902842296615269186L;

	public UnexpectedInputException() {
		super();
	}

	public UnexpectedInputException(String message) {
		super(message);
	}

	public UnexpectedInputException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnexpectedInputException(Throwable cause) {
		super(cause);
	}
}
