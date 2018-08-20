/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.util.view;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 28 - Nov - 2016
 */
public final class ActionBarUtils {
    public static void setTitle(final ActionBar actionBar, final CharSequence title) {
        if (null == actionBar) {
            return;
        }

        actionBar.setTitle(title);
    }

    public static void setTitleMarquee(final Toolbar toolbar) {
        TextView titleTextView = ActionBarUtils.getTitleTextView(toolbar);
        titleTextView.setSingleLine();
        titleTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        titleTextView.setMarqueeRepeatLimit(-1);
        titleTextView.setHorizontallyScrolling(true);
        titleTextView.setFreezesText(true);
        titleTextView.setSelected(true);
    }

    public static TextView getTitleTextView(final Toolbar toolbar) {
        try {
            Field f = toolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            TextView titleTextView = (TextView) f.get(toolbar);
            f.setAccessible(false);
            return titleTextView;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Support this case", e);
        }
    }

    public static void show(final ActionBar actionBar) {
        if (null == actionBar) {
            return;
        }

        actionBar.show();
    }

    public static void hide(final ActionBar actionBar) {
        if (null == actionBar) {
            return;
        }

        actionBar.hide();
    }

    public static void setDisplayHomeAsUpEnabled(final ActionBar actionBar, final boolean flag) {
        if (null == actionBar) {
            return;
        }

        actionBar.setDisplayHomeAsUpEnabled(flag);
    }

    public static void setDisplayShowHomeEnabled(final ActionBar actionBar, final boolean flag) {
        if (null == actionBar) {
            return;
        }

        actionBar.setDisplayShowHomeEnabled(flag);
    }

    private ActionBarUtils() { }
}
