/*
 * Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
 * 
 * Read LICENCE file in project root for licence terms of this software.
 */
package com.github.francescojo.appdeploy.controller.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.francescojo.appdeploy.exception.AjaxResponseException;

/**
 * @author Francesco Jo
 * @since 21 - Dec - 2014
 */
@Controller
public class ExceptionController implements ErrorController {
	private static final Log LOGGER = LogFactory.getLog(ExceptionController.class);

	@RequestMapping("/error")
	public String error(Model model, HttpServletRequest req, HttpServletResponse resp) {
		ErrorPageDispatcher dispatcher = ErrorPageDispatcher.byException(getException(req));
		LOGGER.error("Using error dispatcher " + dispatcher + " for request: " + req.getRequestURI());

		dispatcher.process(model, req, resp);
		return dispatcher.getErrorPath();
	}

	@Override
	public String getErrorPath() {
		return "common/error.jsp";
	}

	static Exception getException(HttpServletRequest req) {
		return (Exception) req.getAttribute(ExceptionControllerAdvice.KEY_GENERATED_EXCEPTION);
	}

	private enum ErrorPageDispatcher {
		AJAX_EXCEPTION(AjaxResponseException.class) {
			@Override
			String getErrorPath() {
				return "common/ajaxResponse.jsp";
			}

			@Override
			void process(Model model, HttpServletRequest req, HttpServletResponse resp) {
				AjaxResponseException ex = (AjaxResponseException) getException(req);
				model.addAttribute("errorModel", ex.asMessageResponseModel());
			}
		},
		FORBIDDEN(UnsupportedOperationException.class) {
			@Override
			String getErrorPath() {
				return DEFAULT.getErrorPath();
			}

			@Override
			void process(Model model, HttpServletRequest req, HttpServletResponse resp) {
				UnsupportedOperationException ex = (UnsupportedOperationException) getException(req);
				model.addAttribute("exceptionTitle", ex.getClass().getSimpleName());
				model.addAttribute("exceptionMessage", ex.getMessage());
			}
		},
		DEFAULT(null) {
			@Override
			String getErrorPath() {
				return "common/error.jsp";
			}

			@Override
			void process(Model model, HttpServletRequest req, HttpServletResponse resp) {
			}
		};

		final Class<? extends Exception> exceptionClass;

		ErrorPageDispatcher(Class<? extends Exception> e) {
			this.exceptionClass = e;
		}

		static ErrorPageDispatcher byException(Exception e) {
			if (null == e) {
				return DEFAULT;
			}

			Class<? extends Exception> inputClass = e.getClass();
			for (ErrorPageDispatcher dispatcher : ErrorPageDispatcher.values()) {
				if (dispatcher.exceptionClass == inputClass) {
					return dispatcher;
				}
			}

			return DEFAULT;
		}

		abstract String getErrorPath();

		abstract void process(Model model, HttpServletRequest req, HttpServletResponse resp);
	}
}
