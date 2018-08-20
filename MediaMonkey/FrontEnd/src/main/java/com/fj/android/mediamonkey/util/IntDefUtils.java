/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.util;

import com.fj.android.mediamonkey.annotation.IntBoolean;
import com.fj.android.mediamonkey.util.lang.CastUtils;
import com.mediamonkey.android.lib.annotation.ContentDisplayType;

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

    public static @ContentDisplayType int Str2ContentDisplayType(final String value,
                                                                 @ContentDisplayType int fallback) {
        int intValue = CastUtils.parseInt(value, fallback);
        switch (intValue) {
            case ContentDisplayType.VERTICAL:
                return ContentDisplayType.VERTICAL;
            case ContentDisplayType.VERTICAL_PAGING:
                return ContentDisplayType.VERTICAL_PAGING;
            case ContentDisplayType.HORIZONTAL:
                return ContentDisplayType.HORIZONTAL;
            default:
                return fallback;
        }
    }

    private IntDefUtils() {}
}
