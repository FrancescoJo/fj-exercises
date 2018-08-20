/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.util.lang;

import java.util.ArrayList;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Oct - 2016
 */
public final class ArrayUtils {
    public static int length(final long[] arr) {
        return null == arr ? 0 : arr.length;
    }

    public static int length(final Object[] arr) {
        return null == arr ? 0 : arr.length;
    }

    /**
     * Converts given <code>arr</code> to an modifiable {@link java.util.ArrayList}.
     * @return {@link java.util.ArrayList} of <code>arr</code> if <code>arr != null</code>, <code>null</code> otherwise.
     */
    public static ArrayList<Long> asList(final long[] arr) {
        if (null == arr) {
            return null;
        }

        ArrayList<Long> list = new ArrayList<>(arr.length);
        for (long v : arr) {
            list.add(v);
        }

        return list;
    }

    private ArrayUtils() { }
}
