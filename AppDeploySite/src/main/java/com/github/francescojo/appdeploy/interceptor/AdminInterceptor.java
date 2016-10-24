/*
 * Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
 * 
 * Read LICENCE file in project root for licence terms of this software.
 */
package com.github.francescojo.appdeploy.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.github.francescojo.appdeploy.ApplicationConfig;
import com.github.francescojo.appdeploy.controller.common.ExceptionControllerAdvice;
import com.github.francescojo.appdeploy.util.PrimitiveUtils;

/**
 * @author Francesco Jo
 * @since 21 - Dec - 2014
 */
public class AdminInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String enabledStr = System.getenv(ApplicationConfig.ENV_KEY_ADMIN_ENABLED);
		if (!PrimitiveUtils.parseBoolean(enabledStr)) {
			Exception e = new UnsupportedOperationException("Currently disabled. Please contact to system administrator.");
			request.setAttribute(ExceptionControllerAdvice.KEY_GENERATED_EXCEPTION, e);
			response.sendError(HttpStatus.FORBIDDEN.value(), "Forbidden");
			return false;
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}
}
