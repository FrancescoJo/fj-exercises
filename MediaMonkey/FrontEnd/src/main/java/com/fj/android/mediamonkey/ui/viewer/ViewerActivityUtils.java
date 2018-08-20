/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.ui.viewer;

import android.annotation.TargetApi;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.view.View;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 31 - Dec - 2016
 */
final class ViewerActivityUtils {
    static boolean isLandscape(final int currentOrientation) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return isLandscapeCompat(currentOrientation);
        } else {
            return isLandscapeJBMR2(currentOrientation);
        }
    }

    static boolean isPortrait(final int currentOrientation) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return isPortraitCompat(currentOrientation);
        } else {
            return isPortraitJBMR2(currentOrientation);
        }
    }

    static int getHideSysUiFlags() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return getHideSysUiFlagsCompat();
        } else {
            return getHideSysUiFlagsK();
        }
    }

    static int getShowSysUiFlags() {
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
    }

    /*
     * Only works if default orientation is portrait -
     * the state of ActivityInfo.SCREEN_ORIENTATION_USER is always differ depends on
     * default orientation
     */
    private static boolean isLandscapeCompat(final int currentOrientation) {
        return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE == currentOrientation ||
                ActivityInfo.SCREEN_ORIENTATION_USER == currentOrientation;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private static boolean isLandscapeJBMR2(final int currentOrientation) {
        return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE == currentOrientation ||
                ActivityInfo.SCREEN_ORIENTATION_USER == currentOrientation ||
                ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE == currentOrientation;
    }

    private static boolean isPortraitCompat(final int currentOrientation) {
        return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT == currentOrientation;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private static boolean isPortraitJBMR2(final int currentOrientation) {
        return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT == currentOrientation ||
                ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT == currentOrientation;
    }

    private static int getHideSysUiFlagsCompat() {
        return View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LOW_PROFILE;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static int getHideSysUiFlagsK() {
        return View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LOW_PROFILE |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
    }

    private ViewerActivityUtils() {
    }
}
