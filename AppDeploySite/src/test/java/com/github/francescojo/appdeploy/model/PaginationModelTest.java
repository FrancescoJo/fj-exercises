package com.github.francescojo.appdeploy.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class PaginationModelTest {
	@Test
	public void testInRange() throws Exception {
		assertModel(new PaginationModel(9, 50, 2, 10), 1, 10, false, true);
		assertModel(new PaginationModel(11, 50, 2, 10), 10, 20, true, true);
		assertModel(new PaginationModel(21, 50, 2, 10), 20, 25, true, false);
	}

	@Test
	public void testInBoundary() throws Exception {
		assertModel(new PaginationModel(10, 50, 2, 10), 1, 10, false, true);
		assertModel(new PaginationModel(20, 50, 2, 10), 10, 20, true, true);
		assertModel(new PaginationModel(25, 50, 2, 10), 20, 25, true, false);
	}

	private void assertModel(PaginationModel model, int lowPage, int highPage, boolean lowerEnabled, boolean higherEnabled) {
		assertEquals(lowPage, model.getLow());
		assertEquals(highPage, model.getHigh());
		assertTrue(lowerEnabled == model.isLowerEnabled());
		assertTrue(higherEnabled == model.isHigherEnabled());
	}
}
