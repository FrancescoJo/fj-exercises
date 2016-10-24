/*
 * Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
 * 
 * Read LICENCE file in project root for licence terms of this software.
 */
package com.github.francescojo.appdeploy.bo;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.francescojo.appdeploy.dto.PlatformDto;
import com.github.francescojo.appdeploy.exception.UnexpectedInputException;
import com.github.francescojo.appdeploy.model.Messages;
import com.github.francescojo.appdeploy.sqlmap.PlatformService;

/**
 * @author Francesco Jo
 * @since 21 - Dec - 2014
 */
public class PlatformBo {
	private static final Log LOGGER = LogFactory.getLog(PlatformBo.class);

	@Autowired
	private PlatformService platforms;

	@Autowired
	private AppBo appBo;

	public List<PlatformDto> getAll() {
		return platforms.selectAll();
	}

	public int getAllCount() {
		return platforms.selectAllCount();
	}

	public void add(PlatformDto platform) {
		int rowCount = platforms.insert(platform);
		if (rowCount == 0) {
			LOGGER.error("Bad user input: " + platform);
			throw new UnexpectedInputException(Messages.ERR_BAD_USER_INPUT.getText());
		}
	}

	public PlatformDto getById(long id) {
		List<PlatformDto> platformList = getByIdList(Collections.singletonList(id));
		if (CollectionUtils.isEmpty(platformList)) {
			return null;
		} else {
			return platformList.get(0);
		}
	}

	public List<PlatformDto> getByIdList(List<Long> idList) {
		return platforms.selectByIdList(idList);
	}

	public void replace(long id, PlatformDto platform) {
		int rowCount = platforms.updateById(id, platform);
		if (rowCount == 0) {
			LOGGER.error("Bad user input: id: " + id + ", platform: " + platform);
			throw new UnexpectedInputException(Messages.ERR_BAD_USER_INPUT.getText());
		}
	}

	public void remove(List<Long> idList) {
		int count = appBo.getAppCountHavingPlatformId(idList);
		if (count > 0) {
			LOGGER.error("Unable to delete platform: " + idList + " because some apps are using it");
			throw new UnexpectedInputException(Messages.ERR_BAD_USER_INPUT.getText());
		}

		int rowCount = platforms.deleteByIdList(idList);
		if (rowCount != idList.size()) {
			LOGGER.error("Delete request and result mismatch: request " + idList + ", affected results: " + rowCount);
		}
	}

	public PlatformDto parsePlatform(Map<String, Object> platformAnswers) {
		PlatformDto platform = new PlatformDto(platformAnswers);
		if (platform.isEmpty()) {
			LOGGER.error("Bad user input: unparsable platform input: " + platformAnswers);
			throw new UnexpectedInputException(Messages.ERR_BAD_USER_INPUT.getText());
		}

		return platform;
	}

	public PlatformService getPlatformDao() {
		return platforms;
	}

	public void setPlatformDao(PlatformService platformDao) {
		this.platforms = platformDao;
	}

	public AppBo getAppBo() {
		return appBo;
	}

	public void setAppBo(AppBo appBo) {
		this.appBo = appBo;
	}
}
