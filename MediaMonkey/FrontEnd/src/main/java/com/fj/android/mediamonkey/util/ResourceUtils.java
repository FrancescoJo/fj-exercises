/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.util;

import android.content.res.Resources;

import com.fj.android.mediamonkey.MediaMonkeyApplication;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 03 - Dec - 2016
 */
public final class ResourceUtils {
    public static String getString(int stringResId, Object... formatArgs) {
        try {
            if (null == formatArgs) {
                return MediaMonkeyApplication.getInstance().getString(stringResId);
            } else {
                return MediaMonkeyApplication.getInstance().getString(stringResId, formatArgs);
            }
        } catch (Resources.NotFoundException e) {
            return "_STRING_0x" + Integer.toHexString(stringResId);
        }
    }

    public static String getPlurals(int pluralsResId, int qty, Object... formatArgs) {
        try {
            Resources res = MediaMonkeyApplication.getInstance().getResources();
            if (null == formatArgs) {
                return res.getQuantityString(pluralsResId, qty);
            } else {
                return res.getQuantityString(pluralsResId, qty, formatArgs);
            }
        } catch (Resources.NotFoundException e) {
            return "_PLURAL" + Integer.toHexString(pluralsResId);
        }
    }

    private ResourceUtils() { }
}
