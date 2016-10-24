/*
 * Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
 * 
 * Read LICENCE file in project root for licence terms of this software.
 */
package com.github.francescojo.appdeploy.controller.admin;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.francescojo.appdeploy.bo.AppBo;
import com.github.francescojo.appdeploy.bo.DistStageBo;
import com.github.francescojo.appdeploy.bo.PlatformBo;
import com.github.francescojo.appdeploy.bo.UploadHistoryBo;
import com.github.francescojo.appdeploy.dto.AppDto;
import com.github.francescojo.appdeploy.dto.DistStageDto;
import com.github.francescojo.appdeploy.dto.PlatformDto;
import com.github.francescojo.appdeploy.exception.AjaxResponseException;
import com.github.francescojo.appdeploy.exception.NotFoundException;
import com.github.francescojo.appdeploy.exception.UnexpectedInputException;
import com.github.francescojo.appdeploy.model.AppViewModel;
import com.github.francescojo.appdeploy.model.Messages;
import com.github.francescojo.appdeploy.model.response.ResultModel;
import com.github.francescojo.appdeploy.util.ControllerUtils;

/**
 * @author Francesco Jo
 * @since 21 - Dec - 2014
 */
@Controller
public class AdminAppController {
	private static final Log LOGGER = LogFactory.getLog(AdminAppController.class);
	private static final String KEY_APP_IDS = "appIds";

	@Autowired
	private PlatformBo platformBo;

	@Autowired
	private DistStageBo distStageBo;

	@Autowired
	private AppBo appBo;

	@Autowired
	private UploadHistoryBo uploadHistoryBo;

	@RequestMapping("/admin/app")
	public String browse(Model model) {
		if (platformBo.getAllCount() == 0 || distStageBo.getAllCount() == 0) {
			return "admin/appNoParent.jsp";
		}
		List<AppDto> appList = appBo.getAll();

		model.addAttribute("appList", appList);
		return "admin/app.jsp";
	}

	@RequestMapping(value = "/admin/app/add", method = RequestMethod.GET)
	public String browseAddGet(Model model) {
		List<DistStageDto> stages = distStageBo.getAll();
		List<PlatformDto> platforms = platformBo.getAll();

		if (CollectionUtils.isEmpty(stages)) {
			return ControllerUtils.alertAndRedirect(model, Messages.ERR_STAGE_NOT_FOUND.getText(), "/admin/app");
		} else if (CollectionUtils.isEmpty(platforms)) {
			return ControllerUtils.alertAndRedirect(model, Messages.ERR_PLATFORM_NOT_FOUND.getText(), "/admin/app");
		}

		model.addAttribute("stageList", stages);
		model.addAttribute("platformList", platforms);

		return "admin/appAdd.jsp";
	}

	@RequestMapping(value = "/admin/app/add", method = RequestMethod.POST)
	public String browseAddPost(@RequestParam Map<String, Object> appAnswers,
			@RequestParam("icon") MultipartFile iconFile, Model model) {
		AppDto appDto = new AppDto(iconFile, appAnswers);
		appBo.add(appDto);

		return ControllerUtils.alertAndRedirect(model, Messages.MSG_APP_ADDED.getText(), "/admin");
	}

	@RequestMapping(value = "/admin/app/check ", method = RequestMethod.POST)
	public @ResponseBody String ajaxCheckDelete(@RequestParam Map<String, Object> appIds) {
		List<Long> appIdList = ControllerUtils.parseIds(MapUtils.getString(appIds, KEY_APP_IDS));
		if (CollectionUtils.isEmpty(appIdList)) {
			LOGGER.error("Empty app id list on checkDelete: " + appIds);
			throw new AjaxResponseException(HttpStatus.BAD_REQUEST, Messages.ERR_BAD_USER_INPUT);
		}

		List<AppDto> appList;
		try {
			appList = appBo.getAppsWithoutUploadHistory(appIdList);
		} catch (UnexpectedInputException e) {
			throw new AjaxResponseException(HttpStatus.BAD_REQUEST, Messages.ERR_BAD_USER_INPUT);
		}

		for (AppDto app : appList) {
			int uploadCount = uploadHistoryBo.getTotalUploadCount(app.getId());
			if (uploadCount > 0) {
				throw new AjaxResponseException(HttpStatus.FOUND, Messages.CONFIRM_DELETE_WITH_UPLOAD_HISTORY);
			}
		}

		ResultModel<Boolean> result = new ResultModel<Boolean>(true);
		return result.toString();
	}

	@RequestMapping(value = "/admin/app/delete", method = RequestMethod.POST)
	public String browseDelete(Model model, @RequestParam Map<String, Object> appIds) {
		List<Long> appIdList = ControllerUtils.parseIds(MapUtils.getString(appIds, KEY_APP_IDS));
		if (CollectionUtils.isEmpty(appIdList)) {
			LOGGER.error("Empty app id list on delete: " + appIds);
			return ControllerUtils.alertAndRedirect(model, Messages.ERR_BAD_USER_INPUT.getText(), "/admin/app");
		}

		appBo.delete(appIdList);
		return ControllerUtils.alertAndRedirect(model, Messages.MSG_APP_DELETED.getText(), "/admin/app");
	}

	@RequestMapping(value = "/admin/app/{appId}", method = RequestMethod.GET)
	public String browseAppGet(@PathVariable String appId, Model model) {
		AppViewModel app = appBo.createViewModel(NumberUtils.toLong(appId, -1));
		if (null == app) {
			throw new NotFoundException(Messages.ERR_APP_NOT_FOUND.getText());
		}
		model.addAttribute("app", app);
		return "admin/appDetails.jsp";
	}

	public PlatformBo getPlatformBo() {
		return platformBo;
	}

	public void setPlatformBo(PlatformBo platformBo) {
		this.platformBo = platformBo;
	}

	public DistStageBo getDistStageBo() {
		return distStageBo;
	}

	public void setDistStageBo(DistStageBo distStageBo) {
		this.distStageBo = distStageBo;
	}

	public AppBo getAppBo() {
		return appBo;
	}

	public void setDistStageBo(AppBo appBo) {
		this.appBo = appBo;
	}

	public UploadHistoryBo getUploadHistoryBo() {
		return uploadHistoryBo;
	}

	public void setUploadHistoryBo(UploadHistoryBo uploadHistoryBo) {
		this.uploadHistoryBo = uploadHistoryBo;
	}
}
