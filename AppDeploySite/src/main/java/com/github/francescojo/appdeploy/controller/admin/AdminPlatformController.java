/*
 * Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
 * 
 * Read LICENCE file in project root for licence terms of this software.
 */
package com.github.francescojo.appdeploy.controller.admin;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.francescojo.appdeploy.bo.PlatformBo;
import com.github.francescojo.appdeploy.dto.PlatformDto;
import com.github.francescojo.appdeploy.exception.UnexpectedInputException;
import com.github.francescojo.appdeploy.model.Messages;
import com.github.francescojo.appdeploy.model.PlatformPresets;
import com.github.francescojo.appdeploy.util.ControllerUtils;

/**
 * @author Francesco Jo
 * @since 21 - Dec - 2014
 */
@Controller
public class AdminPlatformController {
	private static final Log LOGGER = LogFactory.getLog(AdminPlatformController.class);

	@Autowired
	private PlatformBo platformBo;

	@RequestMapping("/admin/platform")
	public String browse(Model model) {
		List<PlatformDto> platformList = platformBo.getAll();

		model.addAttribute("platformList", platformList);
		return "admin/platform.jsp";
	}

	@RequestMapping(value = "/admin/platform/add", method = RequestMethod.GET)
	public String browseAddGet(Model model) {
		model.addAttribute("platformPresets", PlatformPresets.asPlatformDtoList());
		return "admin/platformAdd.jsp";
	}

	@RequestMapping(value = "/admin/platform/add", method = RequestMethod.POST)
	public String browseAddPost(@RequestParam Map<String, Object> platformAnswers, Model model) {
		PlatformDto platform = platformBo.parsePlatform(platformAnswers);
		platformBo.add(platform);
		return ControllerUtils.alertAndRedirect(model, Messages.MSG_PLATFORM_ADDED.getText(), "/admin");
	}

	@RequestMapping(value = "/admin/platform/{id}", method = RequestMethod.GET)
	public String browseDetailGet(@PathVariable String id, Model model) {
		long idLong = getIdFromPathVariable(id);

		PlatformDto platform = platformBo.getById(idLong);
		if (null == platform) {
			LOGGER.error("Bad user input: no matching platform with " + idLong);
			throw new UnexpectedInputException(Messages.ERR_BAD_USER_INPUT.getText());
		}
		model.addAttribute("platform", platform);

		return "admin/platformDetails.jsp";
	}

	@RequestMapping(value = "/admin/platform/{id}", method = RequestMethod.POST)
	public String browseDetailPost(@PathVariable String id, @RequestParam Map<String, Object> platformAnswers, Model model) {
		long idLong = getIdFromPathVariable(id);

		PlatformDto platform = platformBo.parsePlatform(platformAnswers);
		platformBo.replace(idLong, platform);
		return ControllerUtils.alertAndRedirect(model, Messages.MSG_PLATFORM_UPDATED.getText(), "/admin");
	}

	@RequestMapping(value = "/admin/platform/delete", method = RequestMethod.POST)
	public String browseDelete(@RequestParam(value = "codes") String ids, Model model) {
		List<Long> idList = ControllerUtils.parseIds(ids);

		platformBo.remove(idList);
		return ControllerUtils.alertAndRedirect(model, Messages.MSG_PLATFORM_DELETED.getText(), "/admin");
	}

	private long getIdFromPathVariable(String pathVariable) {
		long idLong = NumberUtils.toLong(pathVariable, -1);
		if (idLong == -1) {
			LOGGER.error("Bad user input: no matching platform with " + pathVariable);
			throw new UnexpectedInputException(Messages.ERR_BAD_USER_INPUT.getText());
		}

		return idLong;
	}

	public PlatformBo getPlatformBo() {
		return platformBo;
	}

	public void setPlatformBo(PlatformBo platformBo) {
		this.platformBo = platformBo;
	}
}
