/*
 * Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
 * 
 * Read LICENCE file in project root for licence terms of this software.
 */
package com.github.francescojo.appdeploy.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.francescojo.appdeploy.bo.AppBo;
import com.github.francescojo.appdeploy.bo.DistStageBo;
import com.github.francescojo.appdeploy.bo.UploadHistoryBo;
import com.github.francescojo.appdeploy.dto.AppDto;
import com.github.francescojo.appdeploy.dto.UploadHistoryDto;
import com.github.francescojo.appdeploy.model.AppViewModel;
import com.github.francescojo.appdeploy.model.PaginationModel;

/**
 * @author Francesco Jo
 * @since 21 - Dec - 2014
 */
@Controller
public class AppController {
	public static final int PAGE_COUNT_PER_WEB_PAGE = 25;

	@Autowired
	private AppBo appBo;

	@Autowired
	private DistStageBo stageBo;

	@Autowired
	private UploadHistoryBo historyBo;

	@RequestMapping("/app/{appId}")
	public String browse(@PathVariable("appId") String appId, Model model) {
		AppDto app = appBo.getById(NumberUtils.toLong(appId, -1L));
		return browseByStageId(appId, Long.toString(app.getStageIdList().get(0)), "1", model);
	}

	@RequestMapping("/app/{appId}/{stageId}")
	public String browseByStageId(@PathVariable("appId") String appId, 
			@PathVariable("stageId") String stageId, 
			@RequestParam(value = "page", required = false) String page,
			Model model) {
		int pageInt = NumberUtils.toInt(page, 1);
		long appIdLong = NumberUtils.toLong(appId, -1L);
		long stageIdLong = NumberUtils.toLong(stageId, -1L);
		
		int totalCount = historyBo.getUploadCount(appIdLong, stageIdLong);
		List<UploadHistoryDto> historyList = historyBo.getHistory(appIdLong, stageIdLong, pageInt);
		Map<String, List<UploadHistoryDto>> groupisedHistory = historyBo.groupByVersion(historyList);
		PaginationModel pageInfo = new PaginationModel(pageInt, totalCount, UploadHistoryBo.DEFAULT_ITEMS_PER_PAGE,
				PAGE_COUNT_PER_WEB_PAGE);

		AppViewModel app = appBo.createViewModel(NumberUtils.toLong(appId, -1L));
		app.setStageId(stageIdLong);
		model.addAttribute("app", app);
		model.addAttribute("historyMap", groupisedHistory);
		model.addAttribute("page", pageInfo);

		return "app.jsp";
	}

	public AppBo getAppBo() {
		return appBo;
	}

	public void setAppBo(AppBo appBo) {
		this.appBo = appBo;
	}

	public DistStageBo getStageBo() {
		return stageBo;
	}

	public void setStageBo(DistStageBo stageBo) {
		this.stageBo = stageBo;
	}

	public UploadHistoryBo getUploadHistoryBo() {
		return historyBo;
	}

	public void setUploadHistoryBo(UploadHistoryBo uploadHistoryBo) {
		this.historyBo = uploadHistoryBo;
	}
}
