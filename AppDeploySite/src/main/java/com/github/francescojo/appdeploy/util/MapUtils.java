/*
 * Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
 * 
 * Read LICENCE file in project root for licence terms of this software.
 */
package com.github.francescojo.appdeploy.util;

import java.util.Map;

/**
 * @author Francesco Jo
 * @since 21 - Dec - 2014
 */
public class MapUtils extends org.apache.commons.collections.MapUtils {
	public static boolean hasKey(Map<?, ?> aMap, String key) {
		return aMap != null && aMap.containsKey(key);
	}
}
