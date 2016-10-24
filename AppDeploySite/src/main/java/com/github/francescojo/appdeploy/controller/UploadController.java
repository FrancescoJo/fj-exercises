/*
 * Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
 * 
 * Read LICENCE file in project root for licence terms of this software.
 */
package com.github.francescojo.appdeploy.controller;

import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.francescojo.appdeploy.bo.AppBo;
import com.github.francescojo.appdeploy.bo.UploadHistoryBo;

/**
 * @author Francesco Jo
 * @since 21 - Dec - 2014
 */
@Controller
public class UploadController {
	@Autowired
	private AppBo appBo;

	@Autowired
	private UploadHistoryBo uploadHistoryBo;

	@RequestMapping(value = "/upload/{appIdStr}/{stageIdStr}", method = RequestMethod.POST)
	public @ResponseBody String uploadPost(@PathVariable String appIdStr, @PathVariable String stageIdStr,
			@RequestParam Map<String, Object> uploadAnswers, @RequestParam("binTray") MultipartFile binFile) {
		long appId = NumberUtils.toLong(appIdStr, -1);
		long stageId = NumberUtils.toLong(stageIdStr, -1);
		return uploadHistoryBo.uploadBinary(appId, stageId, uploadAnswers, binFile);
	}

	public AppBo getAppBo() {
		return appBo;
	}

	public void setAppBo(AppBo appBo) {
		this.appBo = appBo;
	}
}
