/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.example.hwan.myapplication._base;

import org.junit.Ignore;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.fail;

/**
 * Common test configuration for Java-only logic.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Aug - 2016
 */
@Ignore("Test base class on Java logic only")
public class JavaTestBase {
    protected static void assertEmpty(Collection<?> collection) {
        assertEmpty("", collection);
    }

    protected static void assertEmpty(final String msg, Collection<?> collection) {
        if (null != collection && collection.size() > 0) {
            String m = msg == null ? "" : msg;

            fail(m + " expected empty, but was:<" + collection + ">");
        }
    }

    protected static void assertListEquals(List<?> expected, List<?> actual) {
        assertListEquals("", expected, actual);
    }

    protected static void assertListEquals(String msg, List<?> expected, List<?> actual) {
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