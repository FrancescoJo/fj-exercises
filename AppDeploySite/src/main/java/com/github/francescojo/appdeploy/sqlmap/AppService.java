/*
 * Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
 * 
 * Read LICENCE file in project root for licence terms of this software.
 */
package com.github.francescojo.appdeploy.sqlmap;

import java.util.List;

import com.github.francescojo.appdeploy.dto.AppDto;

/**
 * @author Francesco Jo
 * @since 21 - Dec - 2014
 */
public interface AppService {
	List<AppDto> selectAll();

	int insert(AppDto app);

	List<AppDto> selectByIdList(List<Long> appIds);
	
	int deleteById(long appIds);
	
	int selectAppCountByPlatformIds(List<Long> platformIds);
	
	AppDto selectById(long appId);
}
