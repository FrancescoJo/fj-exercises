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
public class PrimitiveUtils {
	public static boolean parseBoolean(String s) {
		if ("1".equals(s) || "y".equalsIgnoreCase(s) || "on".equalsIgnoreCase(s)) {
			return true;
		} else if ("0".equals(s) || "n".equalsIgnoreCase(s) || "off".equalsIgnoreCase(s)) {
			return false;
		} else {
			return Boolean.parseBoolean(s);
		}
	}
}
