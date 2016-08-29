/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.github.francescojo.awesome.sqlmap;

import com.github.francescojo.awesome.dto.CounterDto;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Aug - 2016
 */
public interface CounterSqlMap {
	CounterDto getCounter();
	
	int updateCounter(CounterDto newCounter);
}
