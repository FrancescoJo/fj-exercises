/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.util.view;

import android.content.res.Resources;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 10 - Oct - 2016
 */
public final class ViewUtils {
    public static float px2Dp(final float px) {
        final float densityDpi = Resources.getSystem().getDisplayMetrics().densityDpi;
        return px / (densityDpi / 160f);
    }

    public static int dp2Px(final float dp) {
        final float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private ViewUtils() {}
}
