/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.ui.common;

import android.widget.Toast;

import com.fj.android.mediamonkey.MediaMonkeyApplication;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 25 - Nov - 2016
 */
public final class ToastHelper {
    public static Toast show(final int message, final int length) {
        return show(MediaMonkeyApplication.getInstance().getString(message), length);
    }

    public static Toast show(final String message, final int length) {
        Toast toast = Toast.makeText(MediaMonkeyApplication.getInstance(), message, length);
        toast.show();

        return toast;
    }

    private ToastHelper() { }
}
