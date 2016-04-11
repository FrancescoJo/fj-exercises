/*
 * @(#)Calculator.java $Version 13 - Nov - 2015
 *
 * Licenced under Apache License v2.0.
 * Read http://www.apache.org/licenses/ for details.
 */
package com.github.francescojo.androidjunitexercise.bo;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 */
public class Calculator {
	public int add(int a, int b) {
		return a + b;
	}

	public int subtract(int a, int b) {
		return a - b;
	}

	public void expectedNPE() {
		throw new NullPointerException();
	}
}
