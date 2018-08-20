/*
 * This software is distributed under no licenses and warranty.
 * Use this software at your own risk.
 */
package com.github.francescojo.gsondbg;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Callable;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 14 - Apr - 2016
 */
public class GsonDebuggableInjectionTest {
	@Test
	public void testInject_differentInstance() {
		GsonDebuggable gson = GsonDebuggable.getInstance();

		GsonDebuggable.setInstanceFactory(new Callable<GsonDebuggable>() {
			@Override
			public GsonDebuggable call() throws Exception {
				// e.g. Mockito.mock(GsonDebuggable.class);
				return new GsonDebuggable();
			}
		});
		GsonDebuggable mockGson = GsonDebuggable.getInstance();

		Assert.assertNotEquals(gson, mockGson);

		// Prevent instance factory side effects on other tests
		GsonDebuggable.setInstanceFactory(null);
	}

	@Test
	public void testInject_sameInstance() {
		final GsonDebuggable gson = GsonDebuggable.getInstance();

		GsonDebuggable.setInstanceFactory(new Callable<GsonDebuggable>() {
			@Override
			public GsonDebuggable call() throws Exception {
				return gson;
			}
		});
		GsonDebuggable mockGson = GsonDebuggable.getInstance();

		Assert.assertEquals(gson, mockGson);

		// Prevent instance factory side effects on other tests
		GsonDebuggable.setInstanceFactory(null);
	}
}
