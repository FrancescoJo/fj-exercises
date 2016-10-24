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
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class DatabaseIOException extends RuntimeException {
	private static final long serialVersionUID = -8228213309223932314L;

	public DatabaseIOException() {
		super();
	}

	public DatabaseIOException(String message) {
		super(message);
	}

	public DatabaseIOException(String message, Throwable cause) {
		super(message, cause);
	}

	public DatabaseIOException(Throwable cause) {
		super(cause);
	}
}
