/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.example.hwan.myapplication.util;

import com.example.hwan.myapplication.annotation.IntBoolean;

/**
 * Utility class for converting <code>int</code> to <code>{@link android.support.annotation.IntDef} int</code>.
 * This class is useless on API Level 24+ configuration that supports default implementation
 * on interfaces, which is introduced in Java 8.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 12 - Oct - 2016
 */
public final class IntDefUtils {
    public static @IntBoolean int int2TrueFalse(final int value) {
        return value == 0 ? IntBoolean.FALSE : IntBoolean.TRUE;
    }

    private IntDefUtils() {}
}
