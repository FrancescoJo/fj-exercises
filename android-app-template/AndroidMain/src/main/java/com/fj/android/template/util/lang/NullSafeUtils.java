/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.fj.android.template.util.lang;

/**
 * Helps some boring null-check and assigning default fallback value cases,
 * to avoid NullPointerException, because of non null-safe nature of Java.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Oct - 2016
 */
public final class NullSafeUtils {
    public static String get(final String str) {
        return get(str, "");
    }

    public static String get(final String str, final String fallback) {
        if (null != str) {
            return str;
        }

        return fallback;
    }

    private NullSafeUtils() {}
}
