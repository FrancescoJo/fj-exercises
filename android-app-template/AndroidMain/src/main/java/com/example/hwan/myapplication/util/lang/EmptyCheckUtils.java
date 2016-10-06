/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.example.hwan.myapplication.util.lang;

import java.util.Collection;

/**
 * Performs null-safe emptiness checks on various objects.
 * Do not use this utility in some contexts when <code>null</code>
 * represents specific meaning.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Oct - 2016
 */
public final class EmptyCheckUtils {
    public static boolean isEmpty(Collection<?> collection) {
        return null == collection || collection.isEmpty();
    }

    public static boolean isEmpty(EmptyObject obj) {
        return null == obj || obj.isEmpty();
    }

    public static boolean isEmpty(final boolean... array) {
        return null == array || 0 == array.length;
    }

    public static boolean isEmpty(final byte... array) {
        return null == array || 0 == array.length;
    }

    public static boolean isEmpty(final char... array) {
        return null == array || 0 == array.length;
    }

    public static boolean isEmpty(final double... array) {
        return null == array || 0 == array.length;
    }

    public static boolean isEmpty(final float... array) {
        return null == array || 0 == array.length;
    }

    public static boolean isEmpty(final int... array) {
        return null == array || 0 == array.length;
    }

    public static boolean isEmpty(final long... array) {
        return null == array || 0 == array.length;
    }

    public static boolean isEmpty(final Object... array) {
        return null == array || 0 == array.length;
    }

    public static boolean isEmpty(final short... array) {
        return null == array || 0 == array.length;
    }

    private EmptyCheckUtils() {}
}
