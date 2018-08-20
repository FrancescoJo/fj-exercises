/*
 * SimpleHttpServer project
 *
 * Read LICENSE file in project root for licence details.
 */
package com.francescojo.simplehttpsvr.util;

/**
 * Performs null-safe emptiness checks on various objects.
 * Do not use this utility in some contexts when <code>null</code>
 * represents specific meaning.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Oct - 2016
 */
public final class EmptyCheckUtils {
    public static boolean isEmpty(final Object... array) {
        return null == array || 0 == array.length;
    }

    private EmptyCheckUtils() {}
}
