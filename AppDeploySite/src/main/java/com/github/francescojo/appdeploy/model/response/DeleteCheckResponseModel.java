/*
 * Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
 * 
 * Read LICENCE file in project root for licence terms of this software.
 */
package com.github.francescojo.appdeploy.model.response;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

/**
 * @author Francesco Jo
 * @since 21 - Dec - 2014
 */
public class DeleteCheckResponseModel extends AbstractBaseResponseModel {
	private final List<Long> deleteIds;

	public DeleteCheckResponseModel() {
		this(null);
	}

	public DeleteCheckResponseModel(List<Long> deleteIds) {
		this.deleteIds = deleteIds;
	}

	@Override
	public boolean isEmpty() {
		return CollectionUtils.isEmpty(deleteIds);
	}

	public List<Long> getDeleteIds() {
		return Collections.unmodifiableList(deleteIds);
	}
}
