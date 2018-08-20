/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.util.lang;

import android.util.SparseArray;

import java.util.Collection;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 16 - Dec - 2016
 */
public final class CollectionUtils {
    public static int size(Collection<?> collection) {
        return null == collection ? 0 : collection.size();
    }

    public static int size(SparseArray<?> sparseArray) {
        return null == sparseArray ? 0 : sparseArray.size();
    }

    private CollectionUtils() { }
}
