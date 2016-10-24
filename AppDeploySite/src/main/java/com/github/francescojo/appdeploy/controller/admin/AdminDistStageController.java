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

import com.github.francescojo.appdeploy.bo.DistStageBo;
import com.github.francescojo.appdeploy.dto.DistStageDto;
import com.github.francescojo.appdeploy.exception.UnexpectedInputException;
import com.github.francescojo.appdeploy.model.DistStagePresets;
import com.github.francescojo.appdeploy.model.Messages;
import com.github.francescojo.appdeploy.util.ControllerUtils;

/**
 * @author Francesco Jo
 * @since 21 - Dec - 2014
 */
@Controller
public class AdminDistStageController {
	private static final Log LOGGER = LogFactory.getLog(AdminPlatformController.class);

	@Autowired
	private DistStageBo distStageBo;

	@RequestMapping("/admin/distStage")
	public String browse(Model model) {
		List<DistStageDto> distStageList = distStageBo.getAll();

		model.addAttribute("distStageList", distStageList);
		return "admin/distStage.jsp";
	}

	@RequestMapping(value = "/admin/distStage/add", method = RequestMethod.GET)
	public String browseDistStageAddGet(Model model) {
		List<DistStageDto> presets = DistStagePresets.asDistStageDtoList();

		model.addAttribute("stagePresets", presets);
		return "admin/distStageAdd.jsp";
	}

	@RequestMapping(value = "/admin/distStage/add", method = RequestMethod.POST)
	public String browseDistStageAddPost(@RequestParam Map<String, Object> stageAnswers, Model model) {
		DistStageDto stage = distStageBo.parseDistStage(stageAnswers);
		
		distStageBo.add(stage);
		return ControllerUtils.alertAndRedirect(model, Messages.MSG_DIST_STAGE_ADDED.getText(), "/admin");
	}

	@RequestMapping(value = "/admin/distStage/delete", method = RequestMethod.POST)
	public String browseDistStageDelete(@RequestParam(value = "codes") String ids, Model model) {
		List<Long> idList = ControllerUtils.parseIds(ids);

		distStageBo.remove(idList);
		return ControllerUtils.alertAndRedirect(model, Messages.MSG_DIST_STAGE_DELETED.getText(), "/admin");
	}

	@RequestMapping(value = "/admin/distStage/{id}", method = RequestMethod.GET)
	public String browseDetailGet(@PathVariable String id, Model model) {
		long idLong = getIdFromPathVariable(id);

		DistStageDto stage = distStageBo.getById(idLong);
		if (null == stage) {
			LOGGER.error("Bad user input: no matching stage with " + idLong);
			throw new UnexpectedInputException(Messages.ERR_BAD_USER_INPUT.getText());
		}
		model.addAttribute("stage", stage);
		return "admin/distStageDetails.jsp";
	}

	@RequestMapping(value = "/admin/distStage/{id}", method = RequestMethod.POST)
	public String browseDetailPost(@PathVariable String id, @RequestParam Map<String, Object> stageAnswers, Model model) {
		long idLong = getIdFromPathVariable(id);

		DistStageDto stage = distStageBo.parseDistStage(stageAnswers);
		distStageBo.replace(idLong, stage);
		return ControllerUtils.alertAndRedirect(model, Messages.MSG_DIST_STAGE_UPDATED.getText(), "/admin");
	}

	private long getIdFromPathVariable(String pathVariable) {
		long idLong = NumberUtils.toLong(pathVariable, -1);
		if (idLong == -1) {
			LOGGER.error("Bad user input: no matching platform with " + pathVariable);
			throw new UnexpectedInputException(Messages.ERR_BAD_USER_INPUT.getText());
		}

		return idLong;
	}

	public DistStageBo getDistStageBo() {
		return distStageBo;
	}

	public void setDistStageBo(DistStageBo distStageBo) {
		this.distStageBo = distStageBo;
	}

}
