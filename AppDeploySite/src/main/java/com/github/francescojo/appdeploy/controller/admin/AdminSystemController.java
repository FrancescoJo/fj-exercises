/*
 * Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
 * 
 * Read LICENCE file in project root for licence terms of this software.
 */
package com.github.francescojo.appdeploy.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.francescojo.appdeploy.Application;

/**
 * @author Francesco Jo
 * @since 21 - Dec - 2014
 */
@Controller
public class AdminSystemController {
	@RequestMapping("/admin/sys")
	public String browse(Model model) {
		return "admin/system.jsp";
	}

	@RequestMapping("/admin/sys/shutdown")
	public void browseShutdown(Model model) {
		Application.getInstance().stopGracefully();
	}
}
