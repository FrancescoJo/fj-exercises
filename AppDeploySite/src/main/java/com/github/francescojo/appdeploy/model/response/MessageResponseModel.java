/*
 * Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
 * 
 * Read LICENCE file in project root for licence terms of this software.
 */
package com.github.francescojo.appdeploy.model.response;

import org.apache.commons.lang3.StringUtils;

import com.github.francescojo.appdeploy.model.Messages;

/**
 * @author Francesco Jo
 * @since 21 - Dec - 2014
 */
public class MessageResponseModel extends AbstractBaseResponseModel {
	public final int code;
	public final String text;
	
	public MessageResponseModel() {
		this(0, null);
	}
	
	public MessageResponseModel(Messages messageDef) {
		this(messageDef.getCode(), messageDef.getText());
	}

	public MessageResponseModel(int code, String text) {
		this.code = code;
		this.text = text;
	}
	
	@Override
	public boolean isEmpty() {
		return 0 == code && StringUtils.isEmpty(text);
	}
}
