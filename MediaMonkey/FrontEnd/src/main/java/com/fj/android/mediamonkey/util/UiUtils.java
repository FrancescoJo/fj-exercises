/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.util;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;

import com.fj.android.mediamonkey.R;

import java.util.Calendar;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 16 - Dec - 2016
 */
public final class UiUtils {
    private static final long DAY_IN_MS = 86400000L;

    public static String getRelativeDateText(long now, long then) {
        long delta = now - then;
        if (delta < 0) {
            return "";
        }

        Calendar nowCal = Calendar.getInstance();
        nowCal.setTimeInMillis(now);
        Calendar thenCal = Calendar.getInstance();
        thenCal.setTimeInMillis(then);

        int y1 = nowCal.get(Calendar.YEAR);
        int y2 = thenCal.get(Calendar.YEAR);
        int m1 = nowCal.get(Calendar.MONTH);
        int m2 = thenCal.get(Calendar.MONTH);
        int d1 = nowCal.get(Calendar.DAY_OF_MONTH);
        int d2 = thenCal.get(Calendar.DAY_OF_MONTH);

        if (y1 == y2 && m1 == m2 && d1 == d2) {
            return ResourceUtils.getString(R.string.text_date_today);
        }

        if (delta > 0 && delta <= DAY_IN_MS) {
            return ResourceUtils.getString(R.string.text_date_yesterday);
        } else {
            int diff = d1 - d2;
            return ResourceUtils.getPlurals(R.plurals.text_date_days_ago, diff, diff);
        }
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(final String text) {
        String nullSafeStr;
        if (null == text) {
            nullSafeStr = "";
        } else {
            nullSafeStr = text;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return Html.fromHtml(nullSafeStr);
        } else {
            return Html.fromHtml(nullSafeStr, Html.FROM_HTML_MODE_LEGACY);
        }
    }


    private UiUtils() { }
}
