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
public class PlatformDto extends AbstractBaseDto {
	public static final String KEY_NAME = "name";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_PLATFORM_ADMIN_URL = "adminUrl";

	private long id;
	private String name;
	private String description;
	private String adminUrl;
	
	public PlatformDto() {
		this(null);
	}

	public PlatformDto (Map<String, Object> params) {
		if (MapUtils.isEmpty(params)) {
			return;
		}

		this.name = MapUtils.getString(params, KEY_NAME, StringUtils.EMPTY);
		this.description = MapUtils.getString(params, KEY_DESCRIPTION, StringUtils.EMPTY);
		this.adminUrl = MapUtils.getString(params, KEY_PLATFORM_ADMIN_URL, StringUtils.EMPTY);
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

	public String getAdminUrl() {
		return adminUrl;
	}

	public void setAdminUrl(String adminUrl) {
		this.adminUrl = adminUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public boolean isEmpty() {
		return 0L == id && StringUtils.isEmpty(name) && StringUtils.isEmpty(description) &&
				StringUtils.isEmpty(adminUrl);
	}
}
