/*
 * Copyright 2014, Francesco Jo(nimbusob@gmail.com). All rights reserved.
 * 
 * Read LICENCE file in project root for licence terms of this software.
 */
package com.github.francescojo.appdeploy.model;

/**
 * @author Francesco Jo
 * @since 21 - Dec - 2014
 */
public class PaginationModel extends AbstractBaseModel {
	private int current;
	private int low;
	private int high;
	private boolean isLowerEnabled;
	private boolean isHigherEnabled;

	public PaginationModel(int currentPage, int totalCount, int itemCountPerPage, int pageCount) {
		this.current = currentPage;
		
		int pageMinimumLimit = (int) Math.ceil((float) currentPage / pageCount);
		int pageMaximumLimit = (int) Math.ceil((float) totalCount / itemCountPerPage);
		this.low = Math.max((pageMinimumLimit - 1) * pageCount, 1);
		this.high = Math.min(pageMaximumLimit, pageMinimumLimit * pageCount);
		this.isLowerEnabled = low != 1;
		this.isHigherEnabled = high < pageMaximumLimit;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	public int getLow() {
		return low;
	}

	public void setLow(int low) {
		this.low = low;
	}

	public int getHigh() {
		return high;
	}

	public void setHigh(int high) {
		this.high = high;
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}

	public boolean isLowerEnabled() {
		return isLowerEnabled;
	}

	public void setLowerEnabled(boolean isLowerEnabled) {
		this.isLowerEnabled = isLowerEnabled;
	}

	public boolean isHigherEnabled() {
		return isHigherEnabled;
	}

	public void setHigherEnabled(boolean isHigherEnabled) {
		this.isHigherEnabled = isHigherEnabled;
	}
}
