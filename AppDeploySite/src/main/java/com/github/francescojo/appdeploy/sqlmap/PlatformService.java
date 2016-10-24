/*
 * Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
 * 
 * Read LICENCE file in project root for licence terms of this software.
 */
package com.github.francescojo.appdeploy.sqlmap;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.github.francescojo.appdeploy.dto.PlatformDto;

/**
 * @author Francesco Jo
 * @since 21 - Dec - 2014
 */
public interface PlatformService {
	List<PlatformDto> selectAll();
	
	int selectAllCount();
	
	List<PlatformDto> selectByIdList(List<Long> id);

	int insert(PlatformDto platform);

	int updateById(@Param("id") long id, @Param("platform") PlatformDto platform);
	
	int deleteByIdList(List<Long> idList);
}
