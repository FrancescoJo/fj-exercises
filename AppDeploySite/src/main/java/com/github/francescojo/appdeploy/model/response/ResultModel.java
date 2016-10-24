/*
 * Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
 * 
 * Read LICENCE file in project root for licence terms of this software.
 */
package com.github.francescojo.appdeploy.model.response;

import com.github.francescojo.appdeploy.util.EmptyObject;

/**
 * @author Francesco Jo
 * @since 21 - Dec - 2014
 */
public class ResultModel<T> extends AbstractBaseResponseModel {
	public final T result;
	public final MessageResponseModel message;
	
	public ResultModel(T result) {
		this(result, null);
	}

	public ResultModel(T result, MessageResponseModel message) {
		this.result = result;
		this.message = message;
	}

	@Override
	public boolean isEmpty() {
		boolean isResultEmpty;
		if (result instanceof EmptyObject) {
			isResultEmpty = null == result || ((EmptyObject) result).isEmpty();
		} else {
			isResultEmpty = null == result;
		}
		boolean isMessageEmpty = null == message || message.isEmpty();
		
		return isResultEmpty && isMessageEmpty;
	}
}
