/*
 * Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
 * 
 * Read LICENCE file in project root for licence terms of this software.
 */
package com.github.francescojo.appdeploy.sqlmap;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.github.francescojo.appdeploy.dto.UploadHistoryDto;

/**
 * @author Francesco Jo
 * @since 21 - Dec - 2014
 */
public interface UploadHistoryService {
	int selectUploadCountByAppId(long appId);

	int insert(UploadHistoryDto history);

	int selectUploadCount(@Param("appId") long appId, @Param("stageId") long stageId);

	List<UploadHistoryDto> selectOldestHistory(@Param("appId") long appId, @Param("stageId") long stageId,
			@Param("count") int count);

	int deleteByIdList(List<Long> historyIdList);
	
	List<UploadHistoryDto> selectByIdList(List<Long> idList);

	List<UploadHistoryDto> selectByAppId(Long appId);

	List<UploadHistoryDto> selectLatestHistory(@Param("appId") long appId, @Param("stageId") long stageId,
			@Param("page") int page, @Param("limitCount") int count);
}
