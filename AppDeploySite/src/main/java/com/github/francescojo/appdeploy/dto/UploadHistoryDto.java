/*
 * Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
 * 
 * Read LICENCE file in project root for licence terms of this software.
 */
package com.github.francescojo.appdeploy.dto;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.github.francescojo.appdeploy.Application;

/**
 * @author Francesco Jo
 * @since 21 - Dec - 2014
 */
public class UploadHistoryDto extends AbstractBaseDto {
	public static final String KEY_APP_ID = "appId";
	public static final String KEY_STAGE_ID = "stageId";

	private long id;
	private long appId;
	private long stageId;
	private String versionName;
	private String binaryName;
	private String description;
	private String tags;
	private long uploadTime;
	private transient List<String> tagList;
	private transient String uploadTimeStr;

	public UploadHistoryDto() {
		this(null, null);
	}

	public UploadHistoryDto(Map<String, Object> uploadAnswers, MultipartFile binFile) {
		if (null == uploadAnswers || null == binFile) {
			return;
		}

		this.appId = MapUtils.getLong(uploadAnswers, KEY_APP_ID, -1L);
		this.stageId = MapUtils.getLong(uploadAnswers, KEY_STAGE_ID, -1L);
		this.versionName = MapUtils.getString(uploadAnswers, "version", "0.0.1");
		this.description = MapUtils.getString(uploadAnswers, "description", "");
		this.tags = MapUtils.getString(uploadAnswers, "tags", "");
		this.uploadTime = System.currentTimeMillis();
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String fileName = binFile.getOriginalFilename();
		int firstIndex = fileName.indexOf(".");
		if (firstIndex == -1) {
			this.binaryName = binFile.getOriginalFilename() + "_" + df.format(uploadTime);
		} else {
			String fileNamePart = fileName.substring(0, firstIndex);
			String extPart = fileName.substring(firstIndex + 1);
			this.binaryName = fileNamePart + "-" + df.format(uploadTime) + "." + extPart;
		}
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getAppId() {
		return appId;
	}

	public void setAppId(long appId) {
		this.appId = appId;
	}

	public long getStageId() {
		return stageId;
	}

	public void setStageId(long stageId) {
		this.stageId = stageId;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getBinaryName() {
		return binaryName;
	}

	public void setBinaryName(String binaryName) {
		this.binaryName = binaryName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTags() {
		return tags;
	}
	
	public List<String> getTagList() {
		return tagList;
	}

	public void setTags(String tags) {
		this.tags = tags;
		if (!StringUtils.isEmpty(tags)) {
			this.tagList = Arrays.asList(tags.split(","));
		}
	}

	public long getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(long uploadTime) {
		this.uploadTime = uploadTime;
		this.uploadTimeStr = Application.getInstance().getDateFormatter().format(new Date(uploadTime));
	}

	public String getUploadTimeStr() {
		return uploadTimeStr;
	}

	@Override
	public boolean isEmpty() {
		return 0L == id && 0L == appId && 0L == stageId &&
			StringUtils.isEmpty(versionName) && StringUtils.isEmpty(binaryName) &&
			StringUtils.isEmpty(description) && StringUtils.isEmpty(tags) &&
			0L == uploadTime;
	}
}
