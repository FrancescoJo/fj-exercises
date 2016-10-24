/*
 * Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
 * 
 * Read LICENCE file in project root for licence terms of this software.
 */
package com.github.francescojo.appdeploy.model;

import java.util.List;

import com.github.francescojo.appdeploy.dto.AppDto;
import com.github.francescojo.appdeploy.dto.DistStageDto;
import com.github.francescojo.appdeploy.dto.PlatformDto;

/**
 * @author Francesco Jo
 * @since 21 - Dec - 2014
 */
public class AppViewModel {
	private long id;
	private long stageId;
	private String iconName;
	private String name;
	private String description;
	private String authToken;
	private List<DistStageDto> stageList;
	private List<PlatformDto> platformList;

	public AppViewModel(AppDto appDto) {
		this.id = appDto.getId();
		this.iconName = appDto.getIconName();
		this.name = appDto.getName();
		this.description = appDto.getDescription();
		this.authToken = appDto.getAuthToken();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getStageId() {
		return stageId;
	}

	public void setStageId(long stageId) {
		this.stageId = stageId;
	}

	public String getIconName() {
		return iconName;
	}

	public void setIconName(String iconName) {
		this.iconName = iconName;
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

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public List<DistStageDto> getStageList() {
		return stageList;
	}

	public void setStageList(List<DistStageDto> stageList) {
		this.stageList = stageList;
	}

	public List<PlatformDto> getPlatformList() {
		return platformList;
	}

	public void setPlatformList(List<PlatformDto> platformList) {
		this.platformList = platformList;
	}
}
