/*
 * Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
 * 
 * Read LICENCE file in project root for licence terms of this software.
 */
package com.github.francescojo.appdeploy.model;

import java.util.ArrayList;
import java.util.List;

import com.github.francescojo.appdeploy.dto.PlatformDto;

/**
 * @author Francesco Jo
 * @since 21 - Dec - 2014
 */
public enum PlatformPresets {
	ANDROID("Android", "Generic Android devices", "https://play.google.com/apps/publish"),
	IOS("iOS", "iOS mobile devices", "https://developer.apple.com/membercenter/index.action"),
	IPAD("iPad", "iPad devices", "https://developer.apple.com/membercenter/index.action"),
	WINDOWS_PHONE("Windows Phone", "Windows mobile devices", "https://dev.windowsphone.com/dashboard"),
	WINDOWS("Windows Desktop", "Windows desktop", "https://dev.windowsphone.com/dashboard"),
	OSX("Mac OSX", "Apple mac desktop", "https://developer.apple.com/membercenter/index.action"),
	LINUX("Generic Linux", "Linux desktop", ""),
	WEBAPP("Web app", "Generic web app deployment", "");

	final String name;
	final String description;
	final String adminUrl;

	PlatformPresets(String s1, String s2, String s3) {
		this.name = s1;
		this.description = s2;
		this.adminUrl = s3;
	}

	public static List<PlatformDto> asPlatformDtoList() {
		List<PlatformDto> platformDtoList = new ArrayList<>();
		for(PlatformPresets preset : PlatformPresets.values()) {
			platformDtoList.add(preset.asPlatformDto());
		}
		return platformDtoList;
	}

	public PlatformDto asPlatformDto() {
		PlatformDto platformDto = new PlatformDto();
		platformDto.setName(this.name);
		platformDto.setDescription(this.description);
		platformDto.setAdminUrl(this.adminUrl);
		return platformDto;
	}
}
