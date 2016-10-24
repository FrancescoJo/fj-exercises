/*
 * Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
 * 
 * Read LICENCE file in project root for licence terms of this software.
 */
package com.github.francescojo.appdeploy.dto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.web.multipart.MultipartFile;

import com.github.francescojo.appdeploy.Application;
import com.github.francescojo.appdeploy.ApplicationConfig;
import com.github.francescojo.appdeploy.exception.UnexpectedInputException;
import com.github.francescojo.appdeploy.model.Messages;
import com.github.francescojo.appdeploy.util.MapUtils;
import com.github.francescojo.appdeploy.util.PrimitiveUtils;

/**
 * @author Francesco Jo
 * @since 21 - Dec - 2014
 */
public class AppDto extends AbstractBaseDto {
	public static final String KEY_NAME = "name";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_STAGE_CHECKBOX = "stageCheckBox";
	public static final String KEY_PLATFORM_RADIO = "platformRadio";

	private long id;
	private String iconName;
	private String name;
	private String description;
	private String stageIds;
	private String platformIds;
	private String authToken;
	private transient List<Long> stageIdList;
	private transient List<Long> platformIdList;

	public AppDto() {
		this(null, null);
	}

	public AppDto(MultipartFile iconFile, Map<String, Object> params) {
		if (null == iconFile || MapUtils.isEmpty(params)) {
			return;
		}

		this.stageIdList = new ArrayList<>();
		this.platformIdList = new ArrayList<>();

		for (Map.Entry<String, Object> param : params.entrySet()) {
			final String key = param.getKey();
			final Object value = param.getValue();

			if (KEY_NAME.equals(key)) {
				this.name = (String) value;
			} else if (KEY_DESCRIPTION.equals(key)) {
				this.description = (String) value;
			} else if (key.contains(KEY_STAGE_CHECKBOX) && PrimitiveUtils.parseBoolean(value.toString())) {
				long stageId = parseLong(key, KEY_STAGE_CHECKBOX);
				this.stageIdList.add(stageId);
				this.stageIds = StringUtils.join(stageIdList, ",");
			} else if (key.contains(KEY_PLATFORM_RADIO) && PrimitiveUtils.parseBoolean(value.toString())) {
				long platformId = parseLong(key, KEY_PLATFORM_RADIO);
				this.platformIdList.add(platformId);
				this.platformIds = StringUtils.join(platformIdList, ",");
			}
		}

		ApplicationConfig appConfig = Application.getInstance().getConfig();
		File iconBasePath = appConfig.getIconFileBasePath();
		File iconFilePath = new File(iconBasePath, UUID.randomUUID().toString());

		if (iconFile == null || iconFile.isEmpty()) {
			return;
		}

		InputStream ios = null;
		FileOutputStream fos = null;

		try {
			ios = iconFile.getInputStream();
			fos = new FileOutputStream(iconFilePath);
			IOUtils.copy(ios, fos);
		} catch (IOException e) {
			String errMsgFormat = Messages.ERR_NO_PRIVILEGES_EXCEPTION.getText();
			throw new SecurityException(String.format(errMsgFormat, iconBasePath));
		} finally {
			IOUtils.closeQuietly(ios);
			IOUtils.closeQuietly(fos);
		}

		this.iconName = iconFilePath.getName();
	}

	private static long parseLong(String key, String subString) {
		long id = NumberUtils.toLong(key.substring(subString.length()));
		if (id == -1) {
			throw new UnexpectedInputException(Messages.ERR_BAD_USER_INPUT.getText());
		}
		return id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public void setName(String appName) {
		this.name = appName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStageIds() {
		return stageIds;
	}

	public void setStageIds(String stageIds) {
		this.stageIds = stageIds;
		this.stageIdList = parseCommaSeparatedList(stageIds);
	}
	
	public List<Long> getStageIdList() {
		return stageIdList;
	}

	public String getPlatformIds() {
		return platformIds;
	}

	public void setPlatformIds(String platforms) {
		this.platformIds = platforms;
		this.platformIdList = parseCommaSeparatedList(platforms);
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public List<Long> getPlatformIdList() {
		return platformIdList;
	}

	private List<Long> parseCommaSeparatedList(String commaConcatnatedStr) {
		String[] ids = commaConcatnatedStr.split(",");
		List<Long> idList = new ArrayList<>();
		for (String id : ids) {
			idList.add(Long.parseLong(id));
		}

		return idList;
	}

	@Override
	public boolean isEmpty() {
		return 0L == id && StringUtils.isEmpty(iconName) && StringUtils.isEmpty(name) &&
				StringUtils.isEmpty(description) && StringUtils.isEmpty(stageIds) &&
				StringUtils.isEmpty(platformIds) && StringUtils.isEmpty(authToken);
	}
}
