/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.github.francescojo.awesome.dto;

import java.sql.Date;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Aug - 2016
 */
public class CounterDto {
	private int counter;
	private Date lastAccessed;

	public int getCounter() {
		return counter;
	}

	public CounterDto setCounter(int counter) {
		this.counter = counter;
		return this;
	}

	public Date getLastAccessed() {
		return lastAccessed;
	}

	public CounterDto setLastAccessed(Date lastAccessed) {
		this.lastAccessed = lastAccessed;
		return this;
	}
}
