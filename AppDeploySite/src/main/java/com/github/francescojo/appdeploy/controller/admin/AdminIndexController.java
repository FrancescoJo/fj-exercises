/*
 * Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
 * 
 * Read LICENCE file in project root for licence terms of this software.
 */
package com.github.francescojo.appdeploy.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Francesco Jo
 * @since 21 - Dec - 2014
 */
@Controller
public class AdminIndexController {
	@RequestMapping("/admin")
	public String browse(Model model) {
		return "admin/index.jsp";
	}
}
