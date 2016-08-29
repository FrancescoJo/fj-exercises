/*
 * @(#)CounterSqlMap.java $Version Friday 26 - Aug. - 2016.
 *
 * Please read licence.txt on project root for licence details.
 */
package com.github.francescojo.awesome.sqlmap;

import com.github.francescojo.awesome.dto.CounterDto;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 */
public interface CounterSqlMap {
	CounterDto getCounter();
	
	int updateCounter(CounterDto newCounter);
}
