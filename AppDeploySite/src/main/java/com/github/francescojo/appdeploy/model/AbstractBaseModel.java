/*
 * Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
 * 
 * Read LICENCE file in project root for licence terms of this software.
 */
package com.github.francescojo.appdeploy.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.github.francescojo.appdeploy.util.EmptyObject;

/**
 * @author Francesco Jo
 * @since 21 - Dec - 2014
 */
public abstract class AbstractBaseModel implements EmptyObject {
	public abstract boolean isEmpty();

	@Override
	public boolean equals(Object o) {
		return EqualsBuilder.reflectionEquals(this, o, false);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
