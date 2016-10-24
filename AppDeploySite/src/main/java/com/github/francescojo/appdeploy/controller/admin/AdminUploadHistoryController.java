/*
 * Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
 * 
 * Read LICENCE file in project root for licence terms of this software.
 */
package com.github.francescojo.appdeploy.controller.admin;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.francescojo.appdeploy.bo.AppBo;
import com.github.francescojo.appdeploy.bo.DistStageBo;
import com.github.francescojo.appdeploy.bo.UploadHistoryBo;
import com.github.francescojo.appdeploy.dto.AppDto;
import com.github.francescojo.appdeploy.dto.DistStageDto;
import com.github.francescojo.appdeploy.dto.UploadHistoryDto;
import com.github.francescojo.appdeploy.model.Messages;
import com.github.francescojo.appdeploy.model.PaginationModel;
import com.github.francescojo.appdeploy.util.ControllerUtils;

/**
 * @author Francesco Jo
 * @since 21 - Dec - 2014
 */
@Controller
public class AdminUploadHistoryController {
	public static final int PAGE_COUNT_PER_WEB_PAGE = 10;

	@Autowired
	private AppBo appBo;

	@Autowired
	private DistStageBo stageBo;

	@Autowired
	private UploadHistoryBo historyBo;

	@RequestMapping(value = "/admin/app/{appId}/{stageId}", method = RequestMethod.GET)
	public String browseHistoryGet(@PathVariable String appId, @PathVariable String stageId,
			@RequestParam(value = "page", required = false) String page, Model model, HttpServletRequest request) {
		int pageInt = NumberUtils.toInt(page, 1);
		AppDto app = appBo.getById(NumberUtils.toLong(appId, -1));
		DistStageDto stage = stageBo.getById(NumberUtils.toLong(stageId, -1));

		model.addAttribute("app", app);
		model.addAttribute("stage", stage);

		try {
			URL url = new URL(request.getRequestURL().toString());
			model.addAttribute("scheme", url.getProtocol());
			model.addAttribute("host", url.getHost());
			int port = url.getPort();
			if (80 != port && 443 != port) {
				model.addAttribute("port", url.getPort());
			}
		} catch (MalformedURLException ignore) {
			// Will never be caught
		}

		int totalCount = historyBo.getUploadCount(app.getId(), stage.getId());
		List<UploadHistoryDto> historyList = historyBo.getHistory(app.getId(), stage.getId(), pageInt);
		Map<String, List<UploadHistoryDto>> groupisedHistory = historyBo.groupByVersion(historyList);
		PaginationModel pageInfo = new PaginationModel(pageInt, totalCount, UploadHistoryBo.DEFAULT_ITEMS_PER_PAGE,
				PAGE_COUNT_PER_WEB_PAGE);

		model.addAttribute("historyMap", groupisedHistory);
		model.addAttribute("page", pageInfo);
		return "admin/uploadHistory.jsp";
	}

	@RequestMapping(value = "/admin/app/{appId}/{stageId}/delete", method = RequestMethod.POST)
	public String browseHistoryDelete(@PathVariable String appId, @PathVariable String stageId,
			@RequestParam(value = "codes", required = false) String codes, Model model, HttpServletRequest request) {
		List<Long> deleteTargetIds = ControllerUtils.parseIds(codes);
		historyBo.remove(NumberUtils.toLong(appId), NumberUtils.toLong(stageId), deleteTargetIds);

		return ControllerUtils.alertAndRedirect(model, Messages.MSG_HISTORY_DELETED.getText(), "/admin/app");
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

	public UploadHistoryBo getHistoryBo() {
		return historyBo;
	}

	public void setHistoryBo(UploadHistoryBo historyBo) {
		this.historyBo = historyBo;
	}
}
