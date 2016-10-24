/*
 * Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
 * 
 * Read LICENCE file in project root for licence terms of this software.
 */
package com.github.francescojo.appdeploy.dto;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.github.francescojo.appdeploy.util.MapUtils;

/**
 * @author Francesco Jo
 * @since 21 - Dec - 2014
 */
public class DistStageDto extends AbstractBaseDto {
	public static final String KEY_NAME = "name";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_PRESERVATION_COUNT = "preserveCount";
	public static final int DEFAULT_PRESERVATION_COUNT = 50;

	private long id;
	private String name;
	private String description;
	private int preserveCount;

	public DistStageDto() {
		this(null);
	}

	public DistStageDto (Map<String, Object> params) {
		if (MapUtils.isEmpty(params)) {
			return;
		}

		this.name = MapUtils.getString(params, KEY_NAME, StringUtils.EMPTY);
		this.description = MapUtils.getString(params, KEY_DESCRIPTION, StringUtils.EMPTY);
		this.preserveCount = MapUtils.getInteger(params, KEY_PRESERVATION_COUNT, DEFAULT_PRESERVATION_COUNT);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPreserveCount() {
		return preserveCount;
	}

	public void setPreserveCount(int count) {
		this.preserveCount = count;
	}

	@Override
	public boolean isEmpty() {
		return 0L == id && StringUtils.isEmpty(name) && StringUtils.isEmpty(description) && 0 == preserveCount;
	}
}
