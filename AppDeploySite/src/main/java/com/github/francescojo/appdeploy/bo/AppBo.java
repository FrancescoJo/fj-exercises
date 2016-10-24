/*
 * Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
 * 
 * Read LICENCE file in project root for licence terms of this software.
 */
package com.github.francescojo.appdeploy.bo;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.francescojo.appdeploy.Application;
import com.github.francescojo.appdeploy.ApplicationConfig;
import com.github.francescojo.appdeploy.dto.AppDto;
import com.github.francescojo.appdeploy.dto.DistStageDto;
import com.github.francescojo.appdeploy.dto.PlatformDto;
import com.github.francescojo.appdeploy.exception.InternalServerErrorException;
import com.github.francescojo.appdeploy.exception.NotFoundException;
import com.github.francescojo.appdeploy.exception.UnexpectedInputException;
import com.github.francescojo.appdeploy.model.AppViewModel;
import com.github.francescojo.appdeploy.model.Messages;
import com.github.francescojo.appdeploy.sqlmap.AppService;
import com.github.francescojo.appdeploy.util.AES256Utils;
import com.github.francescojo.appdeploy.util.AES256Utils.KeyLengthSpec;

/**
 * @author Francesco Jo
 * @since 21 - Dec - 2014
 */
public class AppBo {
	private static final Log LOGGER = LogFactory.getLog(AppBo.class);

	@Autowired
	private AppService appService;
	
	@Autowired
	private DistStageBo stageBo;
	
	@Autowired
	private PlatformBo platformBo;

	@Autowired
	private UploadHistoryBo uploadHistoryBo;

	public List<AppDto> getAll() {
		return appService.selectAll();
	}

	public void add(AppDto app) {
		String appNameAsToken = app.getName().substring(0, Math.min(app.getName().length(), 32));
		String appTokenPlainText = app.getId() + "|";
		try {
			/*
			 * Save this key to decode auth token;
			 * however we won't going to decrypt it, just String compare it.
			 */
			String key = AES256Utils.generateKey(KeyLengthSpec.AES_256, appNameAsToken);
			String authKey = AES256Utils.encode(key, appTokenPlainText);
			app.setAuthToken(authKey);
		} catch (GeneralSecurityException e) {
			LOGGER.error("Unable to generate auth key for app: " + app + ", unable to upload binaries for this project!!");
			throw new InternalServerErrorException(Messages.ERR_ENCRYPTION_ERROR.getText());
		}

		int rowCount = appService.insert(app);
		if (rowCount == 0) {
			LOGGER.error("Bad user input: " + app);
			throw new UnexpectedInputException(Messages.ERR_BAD_USER_INPUT.getText());
		}
	}

	public List<AppDto> getAppsWithoutUploadHistory(List<Long> appIdList) {
		List<AppDto> appList = appService.selectByIdList(appIdList);
		if (CollectionUtils.isEmpty(appList)) {
			LOGGER.error("Trying to lookup non-existent app ids: " + appIdList);
			throw new UnexpectedInputException(Messages.ERR_BAD_USER_INPUT.getText());
		} else if (appIdList.size() != appList.size()) {
			LOGGER.error("Some apps with id " + appIdList + " is not found.");
			throw new UnexpectedInputException(Messages.ERR_BAD_USER_INPUT.getText());
		}

		return appList;
	}

	public void delete(List<Long> appIdList) {
		List<AppDto> appList = appService.selectByIdList(appIdList);
		if (CollectionUtils.isEmpty(appList)) {
			LOGGER.error("Trying to delete non-existent app ids: " + appIdList);
			throw new UnexpectedInputException(Messages.ERR_BAD_USER_INPUT.getText());
		}
		
		// Deleting file first since storage capacity is more important than app-info data.
		ApplicationConfig appConfig = Application.getInstance().getConfig();
		for (AppDto app : appList) {
			if (!StringUtils.isEmpty(app.getIconName())) {
				File iconFile = new File(appConfig.getIconFileBasePath(), app.getIconName());

				if (!iconFile.delete()) {
					LOGGER.warn("Unable to delete icon file " + iconFile);
				}
			}

			for (long stageId : app.getStageIdList()) {
				File binDir = appConfig.getAppBinaryBasePath(app.getId(), stageId);
				try {
					FileUtils.deleteDirectory(binDir);
				} catch (IOException e) {
					LOGGER.warn("Unable to delete directory " + binDir, e);
				}
			}

			uploadHistoryBo.removeByAppId(app.getId());
			appService.deleteById(app.getId());
		}
	}

	public int getAppCountHavingPlatformId(List<Long> platformIds) {
		return appService.selectAppCountByPlatformIds(platformIds);
	}
	
	public AppDto getById(long appId) {
		return appService.selectById(appId);
	}
	
	public AppViewModel createViewModel(long appId) {
		AppDto app = getById(appId);
		if (null == app) {
			throw new NotFoundException(Messages.ERR_APP_NOT_FOUND.getText());
		}
		AppViewModel appModel = new AppViewModel(app);

		List<DistStageDto> stages = stageBo.getByIdList(app.getStageIdList());
		List<PlatformDto> platforms = platformBo.getByIdList(app.getPlatformIdList());

		appModel.setStageList(stages);
		appModel.setPlatformList(platforms);
		return appModel;
	}

	public AppService getAppService() {
		return appService;
	}

	public void setAppService(AppService appService) {
		this.appService = appService;
	}

	public DistStageBo getStageBo() {
		return stageBo;
	}

	public void setStageBo(DistStageBo stageBo) {
		this.stageBo = stageBo;
	}

	public PlatformBo getPlatformBo() {
		return platformBo;
	}

	public void setPlatformBo(PlatformBo platformBo) {
		this.platformBo = platformBo;
	}

	public UploadHistoryBo getUploadHistoryBo() {
		return uploadHistoryBo;
	}

	public void getUploadHistoryBo(UploadHistoryBo uploadHistoryBo) {
		this.uploadHistoryBo = uploadHistoryBo;
	}
}
