/*
 * @(#)CounterBo.java $Version Friday 26 - Aug. - 2016.
 *
 * Please read licence.txt on project root for licence details.
 */
package com.github.francescojo.awesome.bo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.francescojo.awesome.dto.CounterDto;
import com.github.francescojo.awesome.sqlmap.CounterSqlMap;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 */
@Component
public class CounterBo {
	@Autowired
	private CounterSqlMap counterSqlMap;
	
	public CounterDto getCounter() {
		return counterSqlMap.getCounter();
	}
	
	public int updateCounter(CounterDto count) {
		return counterSqlMap.updateCounter(count);
	}

	public CounterSqlMap getCounterSqlMap() {
		return counterSqlMap;
	}

	public void setCounterSqlMap(CounterSqlMap counterSqlMap) {
		this.counterSqlMap = counterSqlMap;
	}
}
