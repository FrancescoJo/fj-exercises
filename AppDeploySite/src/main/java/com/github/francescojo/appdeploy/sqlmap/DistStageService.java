/*
 * Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
 * 
 * Read LICENCE file in project root for licence terms of this software.
 */
package com.github.francescojo.appdeploy.sqlmap;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.github.francescojo.appdeploy.dto.DistStageDto;

/**
 * @author Francesco Jo
 * @since 21 - Dec - 2014
 */
public interface DistStageService {
	List<DistStageDto> selectAll();
	
	int selectAllCount();
	
	List<DistStageDto> selectByIdList(List<Long> id);
	
	int insert(DistStageDto stage);
	
	int updateById(@Param("id") long id, @Param("distStage") DistStageDto distStage);
	
	int deleteByIdList(List<Long> idList);
}
