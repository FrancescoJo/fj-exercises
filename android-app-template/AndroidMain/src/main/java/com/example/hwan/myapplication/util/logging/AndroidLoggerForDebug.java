/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.example.hwan.myapplication.util.logging;

import android.support.annotation.NonNull;

import com.example.hwan.myapplication.BuildConfig;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 01 - Sep - 2016
 */
class AndroidLoggerForDebug extends AndroidLogger {
    AndroidLoggerForDebug(final String tagName) {
        super(tagName);
    }

    @Override
    public void v(final @NonNull String format, final Object... args) {
        if (BuildConfig.DEBUG) {
            super.v(format, args);
        }
    }

    @Override
    public void v(final Throwable t, final @NonNull String format, final Object... args) {
        if (BuildConfig.DEBUG) {
            super.v(t, format, args);
        }
    }

    @Override
    public void v(final Object obj) {
        if (BuildConfig.DEBUG) {
            super.v(obj);
        }
    }

    @Override
    public void d(final @NonNull String format, final Object... args) {
        if (BuildConfig.DEBUG) {
            super.d(format, args);
        }
    }

    @Override
    public void d(final Throwable t, final @NonNull String format, final Object... args) {
        if (BuildConfig.DEBUG) {
            super.d(t, format, args);
        }
    }

    @Override
    public void d(final Object obj) {
        if (BuildConfig.DEBUG) {
            super.d(obj);
        }
    }

    @Override
    public void i(final @NonNull String format, final Object... args) {
        if (BuildConfig.DEBUG) {
            super.i(format, args);
        }
    }

    @Override
    public void i(final Throwable t, final @NonNull String format, final Object... args) {
        if (BuildConfig.DEBUG) {
            super.i(t, format, args);
        }
    }

    @Override
    public void i(final Object obj) {
        if (BuildConfig.DEBUG) {
            super.i(obj);
        }
    }

    @Override
    public void w(final @NonNull String format, final Object... args) {
        if (BuildConfig.DEBUG) {
            super.w(format, args);
        }
    }

    @Override
    public void w(final Throwable t, final @NonNull String format, final Object... args) {
        if (BuildConfig.DEBUG) {
            super.w(t, format, args);
        }
    }

    @Override
    public void w(final Object obj) {
        if (BuildConfig.DEBUG) {
            super.w(obj);
        }
    }

    @Override
    public void e(final @NonNull String format, final Object... args) {
        if (BuildConfig.DEBUG) {
            super.e(format, args);
        }
    }

    @Override
    public void e(final Throwable t, final @NonNull String format, final Object... args) {
        if (BuildConfig.DEBUG) {
            super.e(t, format, args);
        }
    }

    @Override
    public void e(final Object obj) {
        if (BuildConfig.DEBUG) {
            super.w(obj);
        }
    }

    @Override
    public void log(final int priority, final @NonNull String message, final Throwable t) {
        if (BuildConfig.DEBUG) {
            super.log(priority, message, t);
        }
    }
}
