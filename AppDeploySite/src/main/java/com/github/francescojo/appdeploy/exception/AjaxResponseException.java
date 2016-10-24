/*
 * Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
 * 
 * Read LICENCE file in project root for licence terms of this software.
 */
package com.github.francescojo.appdeploy.exception;

import org.springframework.http.HttpStatus;

import com.github.francescojo.appdeploy.model.Messages;
import com.github.francescojo.appdeploy.model.response.MessageResponseModel;

/**
 * @author Francesco Jo
 * @since 21 - Dec - 2014
 */
public class AjaxResponseException extends RuntimeException {
	private static final long serialVersionUID = -6393116899791165401L;

	public final HttpStatus httpStatus;

	public AjaxResponseException(HttpStatus httpStatus, Messages message) {
		this(httpStatus, message.getText());
	}

	public AjaxResponseException(HttpStatus httpStatus, String errorMsg) {
		this(httpStatus, errorMsg, null);
	}

	public AjaxResponseException(HttpStatus httpStatus, String errorMsg, Throwable cause) {
		super(errorMsg, cause);
		
		this.httpStatus = httpStatus;
	}
	
	public MessageResponseModel asMessageResponseModel() {
		return new MessageResponseModel(httpStatus.value(), getMessage());
	}
}
