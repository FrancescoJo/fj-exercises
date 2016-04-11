/*
 * @(#)JavaTestBase.java $Version 13 - Nov - 2015
 *
 * Licenced under Apache License v2.0.
 * Read http://www.apache.org/licenses/ for details.
 */
package com.github.francescojo.androidjunitexercise._testbase;

import org.junit.Ignore;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.fail;

/**
 * @author Hwan Jo(nimbusob@gmail.com)
 */
@Ignore("Test base class on Java logic only")
public class JavaTestBase {
    public static void assertEmpty(Collection<?> collection) {
        assertEmpty("", collection);
    }

    public static void assertEmpty(final String msg, Collection<?> collection) {
        if (null != collection && collection.size() > 0) {
            String m = msg == null ? "" : msg;

            fail(m + " expected empty, but was:<" + collection + ">");
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
            fail(m + " expected " + expected + ", but was:<" + actual + ">");
        }

        final int size = actual.size();
        for (int i = 0; i < size; i++) {
            Object o1 = expected.get(i);
            Object o2 = actual.get(i);

            if (!o1.equals(o2)) {
                fail(m + " list index " + i + ": expected " + o1 + ", but was:<" + o2 + ">");
            }
        }
    }
}
