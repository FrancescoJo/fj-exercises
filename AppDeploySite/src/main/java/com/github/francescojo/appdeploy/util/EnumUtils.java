/*
 * Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
 * 
 * Read LICENCE file in project root for licence terms of this software.
 */
package com.github.francescojo.appdeploy.util;

/**
 * @author Francesco Jo
 * @since 21 - Dec - 2014
 */
public class EnumUtils extends org.apache.commons.lang3.EnumUtils {
	public static <T extends Enum<T>> T valueOf(Class<T> enumType, String name) {
		return valueOf(enumType, name, null);
	}

	public static <T extends Enum<T>> T valueOf(Class<T> enumType, String name, T defaultValue) {
		if (null == enumType || null == name) {
			return defaultValue;
		}

		try {
			return Enum.valueOf(enumType, name);
		} catch (IllegalArgumentException e) {
			return defaultValue;
		}
	}
}
