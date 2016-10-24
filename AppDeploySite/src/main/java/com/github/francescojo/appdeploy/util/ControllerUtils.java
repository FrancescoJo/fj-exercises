/*
 * Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
 * 
 * Read LICENCE file in project root for licence terms of this software.
 */
package com.github.francescojo.appdeploy.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ui.Model;

import com.github.francescojo.appdeploy.exception.UnexpectedInputException;
import com.github.francescojo.appdeploy.model.Messages;

/**
 * @author Francesco Jo
 * @since 21 - Dec - 2014
 */
public class ControllerUtils {
	private static final Log LOGGER = LogFactory.getLog(ControllerUtils.class);

	private static final String KEY_MESSAGE = "message";
	private static final String KEY_REDIRECT_PATH = "redirectPath";

	public static String redirectTo(Model model, String message, String redirectPath) {
		model.addAttribute(KEY_MESSAGE, message);
		model.addAttribute(KEY_REDIRECT_PATH, redirectPath);
		return "common/alertAndRedirect.jsp";
	}

	public static String alertAndRedirect(Model model, String message, String redirectPath) {
		model.addAttribute(KEY_MESSAGE, message);
		model.addAttribute(KEY_REDIRECT_PATH, redirectPath);
		return "common/alertAndRedirect.jsp";
	}

	public static String alertAndBack(Model model, String message) {
		model.addAttribute(KEY_MESSAGE, message);
		return "common/alertAndBack.jsp";
	}

	public static List<Long> parseIds(String idArrayNotation) {
		if (StringUtils.isEmpty(idArrayNotation)) {
			@SuppressWarnings("unchecked")
			List<Long> tempList = (List<Long>) Collections.EMPTY_LIST;
			return tempList;
		}

		List<Long> idList = new ArrayList<>();
		String[] parsedIds = idArrayNotation.split(",");
		for (String idStr : parsedIds) {
			long id = NumberUtils.toLong(idStr, -1);
			if (id == -1) {
				LOGGER.error("Bad user input idArrayNotation: " + idArrayNotation);
				throw new UnexpectedInputException(Messages.ERR_BAD_USER_INPUT.getText());
			}

			idList.add(id);
		}

		return idList;
	}
}
