/*
 * Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
 * 
 * Read LICENCE file in project root for licence terms of this software.
 */
package com.github.francescojo.appdeploy.controller.common;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.github.francescojo.appdeploy.exception.AjaxResponseException;

/**
 * @author Francesco Jo
 * @since 21 - Dec - 2014
 */
@ControllerAdvice
public class ExceptionControllerAdvice {
	public static final String KEY_GENERATED_EXCEPTION = ExceptionControllerAdvice.class.getName()
			+ "$GeneratedException";

	private static final Log LOGGER = LogFactory.getLog(ExceptionControllerAdvice.class);

	@ExceptionHandler({ AjaxResponseException.class })
	public Model handleAjaxException(Model model, HttpServletRequest req, HttpServletResponse resp,
			AjaxResponseException exception) throws IOException {
		LOGGER.error("Processing ajax response exception");

		resp.sendError(exception.httpStatus.value(), exception.getMessage());
		req.setAttribute(KEY_GENERATED_EXCEPTION, exception);

		return model;
	}

	@ExceptionHandler({ Exception.class, RuntimeException.class })
	public Model handleError(Model model, HttpServletRequest req, HttpServletResponse resp, Exception exception)
			throws IOException {
		LOGGER.error("Request " + req.getRequestURI() + " raised an exception: ", exception);

		model.addAttribute("exceptionTitle", exception.getClass().getSimpleName());
		model.addAttribute("exceptionMessage", exception.getMessage());

		ResponseStatus respStatus = exception.getClass().getAnnotation(ResponseStatus.class);
		if (respStatus == null) {
			resp.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
		} else {
			resp.sendError(respStatus.value().value(), exception.getMessage());
		}

		return model;
	}
}
