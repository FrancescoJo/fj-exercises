/*
 * Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
 * 
 * Read LICENCE file in project root for licence terms of this software.
 */
package com.github.francescojo.appdeploy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.francescojo.appdeploy.bo.AppBo;
import com.github.francescojo.appdeploy.dto.AppDto;

/**
 * @author Francesco Jo
 * @since 21 - Dec - 2014
 */
@Controller
public class IndexController {
	@Autowired
	private AppBo appBo;
	
	@RequestMapping("/")
	public String browse(Model model) {
		List<AppDto> appList = appBo.getAll();
		model.addAttribute("appList", appList);
		return "index.jsp";
	}

	public AppBo getAppBo() {
		return appBo;
	}

	public void setAppBo(AppBo appBo) {
		this.appBo = appBo;
	}
}
