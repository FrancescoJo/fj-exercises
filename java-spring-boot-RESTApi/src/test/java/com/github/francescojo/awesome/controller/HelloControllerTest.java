package com.github.francescojo.awesome.controller;

import org.junit.Test;

import com.github.francescojo.awesome.controller.IndexController;

import static org.junit.Assert.*;

public class HelloControllerTest {
	@Test
	public void testSomeLibraryMethod() {
		IndexController classUnderTest = new IndexController();
		assertEquals("index.jsp", classUnderTest.root(null));
	}
}
