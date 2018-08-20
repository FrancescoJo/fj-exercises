/*
 * Copyright 2016, Francesco Jo(nimbusob@gmail.com). All rights reserved.
 * 
 * Read LICENCE file in project root for licence terms of this software.
 */
package com.github.francescojo.test;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 14 - Apr - 2016
 */
public class JavaTestBase {
	public static void assertEmpty(Collection<?> collection) {
		assertEmpty("", collection);
	}

	public static void assertEmpty(final String msg, Collection<?> collection) {
		if (null != collection && collection.size() > 0) {
			String m = msg == null ? "" : msg;

			throw new AssertionError(m + " expected empty, but was:<" + collection + ">");
		}
	}

	public static void assertListEquals(List<?> expected, List<?> actual) {
		assertListEquals("", expected, actual);
	}

	public static void assertListEquals(String msg, List<?> expected, List<?> actual) {
		if (null == expected && null == actual) {
			return;
		}
		String m = msg == null ? "" : msg;

		if (null == expected || null == actual || expected.size() != actual.size()) {
			throw new AssertionError(m + " expected " + expected + ", but was:<" + actual + ">");
		}

		final int size = actual.size();
		for (int i = 0; i < size; i++) {
			Object o1 = expected.get(i);
			Object o2 = actual.get(i);

			if (!o1.equals(o2)) {
				throw new AssertionError(m + " list index " + i + ": expected " + o1 + ", but was:<" + o2 + ">");
			}
		}
	}

	protected static String getResourceAsString(Package targetPackage, String resourceName) throws IOException {
		InputStream is = getResourceAsStream(targetPackage, resourceName);
		return inputStreamToString(is, Charset.forName("UTF-8"));
	}

	protected static InputStream getResourceAsStream(Package targetPackage, String resourceName) throws IOException {
		ClassLoader l = JavaTestBase.class.getClassLoader();
		String pkgPath = targetPackage.getName().replaceAll("[.]", File.separator);
		return l.getResourceAsStream(pkgPath + File.separator + resourceName);
	}

	private static String inputStreamToString(InputStream is, Charset charSet) throws IOException {
		if (is == null || charSet == null) {
			return "";
		}

		char[] buf = new char[16 * 1024];
		StringBuilder sb = new StringBuilder();
		InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(is, charSet);
			int nRead;
			while ((nRead = isr.read(buf, 0, buf.length)) != -1) {
				sb.append(buf, 0, nRead);
			}
		} finally {
			closeQuietly(isr);
		}

		return sb.toString();
	}

	private static void closeQuietly(Closeable closeable) {
		if (null == closeable) {
			return;
		}

		try {
			closeable.close();
		} catch (Exception ignore) {
		}
	}
}
