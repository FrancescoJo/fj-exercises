/*
 * Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
 * 
 * Read LICENCE file in project root for licence terms of this software.
 */
package com.github.francescojo.appdeploy.bo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.executor.ExecutorException;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.github.francescojo.appdeploy.Application;
import com.github.francescojo.appdeploy.ApplicationConfig;
import com.github.francescojo.appdeploy.dto.AppDto;
import com.github.francescojo.appdeploy.dto.DistStageDto;
import com.github.francescojo.appdeploy.dto.UploadHistoryDto;
import com.github.francescojo.appdeploy.sqlmap.UploadHistoryService;

/**
 * @author Francesco Jo
 * @since 21 - Dec - 2014
 */
public class UploadHistoryBo {
	public static final int DEFAULT_ITEMS_PER_PAGE = 20;
	private static final Log LOGGER = LogFactory.getLog(UploadHistoryBo.class);

	@Autowired
	private AppBo appBo;

	@Autowired
	private DistStageBo stageBo;

	@Autowired
	private UploadHistoryService uploadHistoryService;

	public int getTotalUploadCount(long appId) {
		return uploadHistoryService.selectUploadCountByAppId(appId);
	}
	
	public int getUploadCount(long appId, long stageId) {
		return uploadHistoryService.selectUploadCount(appId, stageId);
	}

	public List<UploadHistoryDto> getHistory(long appId, long stageId, int page) {
		int pageInt = page - 1;
		return uploadHistoryService.selectLatestHistory(appId, stageId, pageInt * DEFAULT_ITEMS_PER_PAGE,
				DEFAULT_ITEMS_PER_PAGE);
	}

	public Map<String, List<UploadHistoryDto>> groupByVersion(List<UploadHistoryDto> historyList) {
		Map<String, List<UploadHistoryDto>> groupisedMap = new TreeMap<>(Collections.reverseOrder((o1, o2) -> {
			return o1.compareTo(o2);
		}));

		for (UploadHistoryDto history : historyList) {
			String versionName = history.getVersionName();
			if (!groupisedMap.containsKey(versionName)) {
				groupisedMap.put(versionName, new ArrayList<>());
			}

			groupisedMap.get(versionName).add(history);
		}

		for (Map.Entry<String, List<UploadHistoryDto>> entry : groupisedMap.entrySet()) {
			Collections.sort(entry.getValue(), (o1, o2) -> {
				// -1 for descending order
				return (int) (o1.getUploadTime() - o2.getUploadTime()) * -1;
			});
		}

		return groupisedMap;
	}

	public String uploadBinary(long appId, long stageId, Map<String, Object> uploadAnswers, MultipartFile binFile) {
		try {
			return uploadBinaryInternal(appId, stageId, uploadAnswers, binFile);
		} catch (MyBatisSystemException | ExecutorException e) {
			LOGGER.error("Exception on underlying database access", e);
			return "ERROR: An unresolved error has been occurred. Consult this with system admin.";
		}
	}

	private String uploadBinaryInternal(long appId, long stageId, Map<String, Object> uploadAnswers,
			MultipartFile binFile) {
		AppDto app = appBo.getById(appId);
		if (null == app) {
			return "ERROR: App with id " + appId + " not found.";
		}
		if (!app.getStageIdList().contains(stageId)) {
			return "ERROR: App does not support stage id " + stageId;
		}
		if (!app.getAuthToken().equals(MapUtils.getString(uploadAnswers, "authToken", ""))) {
			return "ERROR: Wrong authentification token";
		}
		if (binFile == null || binFile.isEmpty()) {
			return "ERROR: No binary file to upload";
		}
		ApplicationConfig config = Application.getInstance().getConfig();
		Path binTrayPath = config.getAppBinaryBasePath(appId, stageId).toPath();
		try {
			FileStore store = Files.getFileStore(binTrayPath);
			if (binFile.getSize() > store.getUsableSpace()) {
				return "ERROR: Upload store is out of space. Consult this with system admin.";
			}
		} catch (IOException e) {
			return "ERROR: Upload store is inaccessible. Consult this with system admin.";
		}
		uploadAnswers.put(UploadHistoryDto.KEY_APP_ID, appId);
		uploadAnswers.put(UploadHistoryDto.KEY_STAGE_ID, stageId);
		UploadHistoryDto historyDto = new UploadHistoryDto(uploadAnswers, binFile);

		String binFilePath = binTrayPath.toFile().getAbsolutePath();
		InputStream ios = null;
		FileOutputStream fos = null;

		try {
			ios = binFile.getInputStream();
			fos = new FileOutputStream(new File(binFilePath, historyDto.getBinaryName()));
			IOUtils.copy(ios, fos);
		} catch (IOException e) {
			return "ERROR: Upload store is accessible, but not modifiable. Consult this with system admin.";
		} finally {
			IOUtils.closeQuietly(ios);
			IOUtils.closeQuietly(fos);
		}
		uploadHistoryService.insert(historyDto);

		DistStageDto stageDto = stageBo.getById(stageId);
		int count = uploadHistoryService.selectUploadCount(appId, stageId);
		int deleteCount = count - stageDto.getPreserveCount();
		if (deleteCount > 0) {
			List<UploadHistoryDto> deleteTargetHistory = uploadHistoryService.selectOldestHistory(appId, stageId,
					deleteCount);
			deleteHistory(binFilePath, deleteTargetHistory);
		}

		return "OK";
	}

	public void remove(long appId, long stageId, List<Long> historyIdList) {
		ApplicationConfig config = Application.getInstance().getConfig();
		String binTrayPath = config.getAppBinaryBasePath(appId, stageId).getAbsolutePath();

		List<UploadHistoryDto> deleteTargets = uploadHistoryService.selectByIdList(historyIdList);
		deleteHistory(binTrayPath, deleteTargets);
	}

	public void removeByAppId(long appId) {
		ApplicationConfig config = Application.getInstance().getConfig();
		String binTrayPath = config.getAppBinaryBasePath().getAbsolutePath();

		List<UploadHistoryDto> deleteTargets = uploadHistoryService.selectByAppId(appId);
		if (CollectionUtils.size(deleteTargets) > 0) {
			deleteHistory(binTrayPath, deleteTargets);
		}
	}

	private void deleteHistory(String binBasePath, List<UploadHistoryDto> deleteTargetHistory) {
		List<Long> deleteTargetIdList = new ArrayList<>(deleteTargetHistory.size());

		for (UploadHistoryDto history : deleteTargetHistory) {
			File binaryFile = new File(binBasePath, history.getBinaryName());
			binaryFile.delete();
			deleteTargetIdList.add(history.getId());
		}

		uploadHistoryService.deleteByIdList(deleteTargetIdList);
	}

	public AppBo getAppBo() {
		return appBo;
	}

	public void setAppBo(AppBo appBo) {
		this.appBo = appBo;
	}

	public UploadHistoryService getUploadHistoryService() {
		return uploadHistoryService;
	}

	public void setUploadHistoryService(UploadHistoryService uploadHistoryService) {
		this.uploadHistoryService = uploadHistoryService;
	}

	public DistStageBo getStageBo() {
		return stageBo;
	}

	public void setStageBo(DistStageBo stageBo) {
		this.stageBo = stageBo;
	}
}
