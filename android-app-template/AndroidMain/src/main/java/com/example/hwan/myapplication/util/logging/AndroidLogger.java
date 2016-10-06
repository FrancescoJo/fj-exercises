/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.example.hwan.myapplication.util.logging;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.example.hwan.myapplication.util.lang.EmptyCheckUtils;

import java.util.Arrays;

/**
 * Android logger. For adjusting logging level,
 * see <a href="https://developer.android.com/reference/android/util/Log.html#isLoggable(java.lang.String, int)">Adjusting loglevel</a> documentation.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 01 - Sep - 2016
 */
class AndroidLogger implements Logger {
    @SuppressWarnings("WeakerAccess")   // Accessed by subclasses
    protected static final String NULL = "null";
    private final String tagName;

    AndroidLogger(final String tagName) {
        this.tagName = tagName;
    }

    @Override
    public void v(final @NonNull String format, final Object... args) {
        if (Log.isLoggable(tagName, Log.VERBOSE)) {
            final String logMsg = formatString(format, args);
            Log.v(tagName, logMsg);
        }
    }

    @Override
    public void v(final Throwable t, final @NonNull String format, final Object... args) {
        if (Log.isLoggable(tagName, Log.VERBOSE)) {
            final String logMsg = formatString(format, args);
            Log.v(tagName, logMsg, t);
        }
    }

    @Override
    public void v(final Object obj) {
        if (Log.isLoggable(tagName, Log.VERBOSE)) {
            final String logMsg = obj == null ? NULL : obj.toString();
            Log.v(tagName, logMsg);
        }
    }

    @Override
    public void d(final @NonNull String format, final Object... args) {
        if (Log.isLoggable(tagName, Log.DEBUG)) {
            final String logMsg = formatString(format, args);
            Log.d(tagName, logMsg);
        }
    }

    @Override
    public void d(final Throwable t, final @NonNull String format, final Object... args) {
        if (Log.isLoggable(tagName, Log.DEBUG)) {
            final String logMsg = formatString(format, args, t);
            Log.d(tagName, logMsg);
        }
    }

    @Override
    public void d(final Object obj) {
        if (Log.isLoggable(tagName, Log.DEBUG)) {
            final String logMsg = obj == null ? NULL : obj.toString();
            Log.d(tagName, logMsg);
        }
    }

    @Override
    public void i(final @NonNull String format, final Object... args) {
        if (Log.isLoggable(tagName, Log.INFO)) {
            final String logMsg = formatString(format, args);
            Log.i(tagName, logMsg);
        }
    }

    @Override
    public void i(final Throwable t, final @NonNull String format, final Object... args) {
        if (Log.isLoggable(tagName, Log.INFO)) {
            final String logMsg = formatString(format, args, t);
            Log.i(tagName, logMsg);
        }
    }

    @Override
    public void i(final Object obj) {
        if (Log.isLoggable(tagName, Log.INFO)) {
            final String logMsg = obj == null ? NULL : obj.toString();
            Log.i(tagName, logMsg);
        }
    }

    @Override
    public void w(final @NonNull String format, final Object... args) {
        if (Log.isLoggable(tagName, Log.WARN)) {
            final String logMsg = formatString(format, args);
            Log.w(tagName, logMsg);
        }
    }

    @Override
    public void w(final Throwable t, final @NonNull String format, final Object... args) {
        if (Log.isLoggable(tagName, Log.WARN)) {
            final String logMsg = formatString(format, args, t);
            Log.w(tagName, logMsg);
        }
    }

    @Override
    public void w(final Object obj) {
        if (Log.isLoggable(tagName, Log.WARN)) {
            final String logMsg = obj == null ? NULL : obj.toString();
            Log.w(tagName, logMsg);
        }
    }

    @Override
    public void e(final @NonNull String format, final Object... args) {
        if (Log.isLoggable(tagName, Log.ERROR)) {
            final String logMsg = formatString(format, args);
            Log.e(tagName, logMsg);
        }
    }

    @Override
    public void e(final Throwable t, final @NonNull String format, final Object... args) {
        if (Log.isLoggable(tagName, Log.ERROR)) {
            final String logMsg = formatString(format, args, t);
            Log.e(tagName, logMsg);
        }
    }

    @Override
    public void e(final Object obj) {
        if (Log.isLoggable(tagName, Log.ERROR)) {
            final String logMsg = obj == null ? NULL : obj.toString();
            Log.e(tagName, logMsg);
        }
    }

    @Override
    public void log(final int priority, final @NonNull String message, final Throwable t) {
        if (Log.isLoggable(tagName, priority)) {
            switch (priority) {
                case Log.VERBOSE:
                    Log.v(tagName, message, t);
                    break;
                case Log.DEBUG:
                    Log.d(tagName, message, t);
                    break;
                case Log.INFO:
                    Log.i(tagName, message, t);
                    break;
                case Log.WARN:
                    Log.w(tagName, message, t);
                    break;
                case Log.ERROR:
                    Log.e(tagName, message, t);
                    break;
                default:
                    if (null == t) {
                        Log.println(priority, tagName, message);
                    } else {
                        Log.println(priority, tagName, message + '\n' + Log.getStackTraceString(t));
                    }
                    break;
            }
        }
    }

    private static String formatString(final String format, final Object... args) {
        if (TextUtils.isEmpty(format)) {
            return NULL + ": " + Arrays.toString(args);
        }

        if (EmptyCheckUtils.isEmpty(args)) {
            return format;
        } else {
            return String.format(format, args);
        }
    }
}
