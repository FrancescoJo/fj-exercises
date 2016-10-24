// Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
AppDeploy.namespace("com.github.francescojo.appdeploy.util.ObjectUtils");

com.github.francescojo.appdeploy.util.ObjectUtils.isEmptyObject = function(obj) {
	if (obj == null) {
		return true;
	}

	if (obj.length > 0) {
		return false;
	}

	if (obj.length === 0) {
		return true;
	}

	for (var key in obj) {
		if (hasOwnProperty.call(obj, key)) {
			return false;
		}
	}

	return true;
}

/**
 * Will throw an exception if given input <code>str</code> type is not a <code>string</code>,
 * be careful.
 */
com.github.francescojo.appdeploy.util.ObjectUtils.isEmptyString = function(str) {
	return (!str || 0 === str.length);
}

/**
 * Will throw an exception if given input <code>str</code> type is not a <code>string</code>,
 * be careful.
 */
com.github.francescojo.appdeploy.util.ObjectUtils.isBlankString = function(str) {
	return (!str || /^\s*$/.test(str));
}