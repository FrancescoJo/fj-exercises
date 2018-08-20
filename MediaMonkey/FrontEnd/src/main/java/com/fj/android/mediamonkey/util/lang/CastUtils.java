/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.util.lang;

import com.mediamonkey.android.lib.internal.Objects;

import timber.log.Timber;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 12 - Jan - 2017
 */
public final class CastUtils {
    public static int parseInt(Object value, int fallback) {
        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            Timber.d("Failed to cast %s into Integer", value);
            return Objects.safeCast(value, Integer.class, fallback);
        }
    }

    private CastUtils() { }
}
