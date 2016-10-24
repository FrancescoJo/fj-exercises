/*
 * Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
 * 
 * Read LICENCE file in project root for licence terms of this software.
 */
package com.github.francescojo.appdeploy.model;

import com.github.francescojo.appdeploy.Application;

/**
 * @author Francesco Jo
 * @since 21 - Dec - 2014
 */
public enum Messages {
	MSG_PLATFORM_ADDED(1, "msg.platform.added"),
	MSG_PLATFORM_UPDATED(2, "msg.platform.updated"),
	MSG_PLATFORM_DELETED(3, "msg.platform.deleted"),
	MSG_DIST_STAGE_ADDED(4, "msg.dist_stage.added"),
	MSG_DIST_STAGE_UPDATED(5, "msg.dist_stage.updated"),
	MSG_DIST_STAGE_DELETED(6, "msg.dist_stage.deleted"),
	MSG_APP_ADDED(7, "msg.app.added"),
	MSG_APP_UPDATED(8, "msg.app.updated"),
	MSG_APP_DELETED(9, "msg.app.deleted"),
	MSG_HISTORY_DELETED(10, "msg.history.deleted"),

	CONFIRM_DELETE_WITH_UPLOAD_HISTORY(1000, "confirm.app.delete_with_uploadhistory"),

	ERR_BAD_USER_INPUT(2000, "error.external.bad_input"),
	ERR_DUPLICATE_PLATFORM_CODE(2001, "error.external.duplicate_platform_code"),
	ERR_APP_NOT_FOUND(2002, "error.external.app_not_found"),
	ERR_STAGE_NOT_FOUND(2003, "error.external.stage_not_found"),
	ERR_PLATFORM_NOT_FOUND(2004, "error.external.platform_not_found"),

	ERR_NO_PRIVILEGES_EXCEPTION(2500, "error.external.no_privileges"),

	ERR_ENCRYPTION_ERROR(3001, "error.internal.encryptor_malfunction"),
	ERR_DECRYPTION_ERROR(3002, "error.internal.decryptor_malfunction");

	final int code;
	final String messageKey;

	Messages(int i, String s) {
		this.code = i;
		this.messageKey = s;
	}

	public int getCode() {
		return code;
	}

	public String getText() {
		return Application.getInstance().getString(messageKey);
	}
}
