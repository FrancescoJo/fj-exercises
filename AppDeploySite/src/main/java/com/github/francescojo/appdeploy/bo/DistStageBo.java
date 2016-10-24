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

import com.github.francescojo.appdeploy.dto.DistStageDto;
import com.github.francescojo.appdeploy.exception.UnexpectedInputException;
import com.github.francescojo.appdeploy.model.Messages;
import com.github.francescojo.appdeploy.sqlmap.DistStageService;

/**
 * @author Francesco Jo
 * @since 21 - Dec - 2014
 */
public class DistStageBo {
	private static final Log LOGGER = LogFactory.getLog(DistStageBo.class);

	@Autowired
	private DistStageService distStages;

	public List<DistStageDto> getAll() {
		return distStages.selectAll();
	}

	public int getAllCount() {
		return distStages.selectAllCount();
	}

	public DistStageDto parseDistStage(Map<String, Object> stageAnswers) {
		DistStageDto stage = new DistStageDto(stageAnswers);
		if (stage.isEmpty()) {
			LOGGER.error("Bad user input: unparsable distStage input: " + stageAnswers);
			throw new UnexpectedInputException(Messages.ERR_BAD_USER_INPUT.getText());
		}

		return stage;
	}

	public void add(DistStageDto stage) {
		int rowCount = distStages.insert(stage);
		if (rowCount == 0) {
			LOGGER.error("Bad user input: " + stage);
			throw new UnexpectedInputException(Messages.ERR_BAD_USER_INPUT.getText());
		}
	}

	public DistStageDto getById(long id) {
		List<DistStageDto> stageList = getByIdList(Collections.singletonList(id));
		if (CollectionUtils.isEmpty(stageList)) {
			return null;
		} else {
			return stageList.get(0);
		}
	}
	
	public List<DistStageDto> getByIdList(List<Long> idList) {
		return distStages.selectByIdList(idList);
	}

	public void replace(long id, DistStageDto stage) {
		int rowCount = distStages.updateById(id, stage);
		if (rowCount == 0) {
			LOGGER.error("Bad user input: id: " + id + ", platform: " + stage);
			throw new UnexpectedInputException(Messages.ERR_BAD_USER_INPUT.getText());
		}
	}

	public void remove(List<Long> idList) {
		int rowCount = distStages.deleteByIdList(idList);
		if (rowCount != idList.size()) {
			LOGGER.error("Delete request and result mismatch: request " + idList + ", affected results: " + rowCount);
		}
	}

	public DistStageService getDistStages() {
		return distStages;
	}

	public void setDistStages(DistStageService distStages) {
		this.distStages = distStages;
	}
}
