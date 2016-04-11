/*
 * @(#)CalculatorTest.java $Version 13 - Nov - 2015
 *
 * Licenced under Apache License v2.0.
 * Read http://www.apache.org/licenses/ for details.
 */
package com.github.francescojo.androidjunitexercise.bo;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 */
public class CalculatorTest {
	private Calculator calculator;

	@Before
	public void setUp() {
		this.calculator = new Calculator();
	}

	@Test
	public void testAdd() throws Exception {
		int num1 = 2;
		int num2 = 5;
		int expected = 7;
		int actual = calculator.add(num1, num2);

		assertEquals(expected, actual);
	}

	@Test
	public void testSubtract() throws Exception {
		int num1 = 35;
		int num2 = 7;
		int expected = 28;
		int actual = calculator.subtract(num1, num2);

		assertEquals(expected, actual);
	}

	@Test(expected = NullPointerException.class)
	public void testExpectedNPE() throws Exception {
		calculator.expectedNPE();
	}
}
