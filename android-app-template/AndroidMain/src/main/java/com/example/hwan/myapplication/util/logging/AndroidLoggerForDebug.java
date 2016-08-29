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
    AndroidLoggerForDebug(String tagName) {
        super(tagName);
    }

    @Override
    public void v(@NonNull String format, Object... args) {
        if (BuildConfig.DEBUG) {
            super.v(format, args);
        }
    }

    @Override
    public void v(Throwable t, @NonNull String format, Object... args) {
        if (BuildConfig.DEBUG) {
            super.v(t, format, args);
        }
    }

    @Override
    public void v(Object obj) {
        if (BuildConfig.DEBUG) {
            super.v(obj);
        }
    }

    @Override
    public void d(@NonNull String format, Object... args) {
        if (BuildConfig.DEBUG) {
            super.d(format, args);
        }
    }

    @Override
    public void d(Throwable t, @NonNull String format, Object... args) {
        if (BuildConfig.DEBUG) {
            super.d(t, format, args);
        }
    }

    @Override
    public void d(Object obj) {
        if (BuildConfig.DEBUG) {
            super.d(obj);
        }
    }

    @Override
    public void i(@NonNull String format, Object... args) {
        if (BuildConfig.DEBUG) {
            super.i(format, args);
        }
    }

    @Override
    public void i(Throwable t, @NonNull String format, Object... args) {
        if (BuildConfig.DEBUG) {
            super.i(t, format, args);
        }
    }

    @Override
    public void i(Object obj) {
        if (BuildConfig.DEBUG) {
            super.i(obj);
        }
    }

    @Override
    public void w(@NonNull String format, Object... args) {
        if (BuildConfig.DEBUG) {
            super.w(format, args);
        }
    }

    @Override
    public void w(Throwable t, @NonNull String format, Object... args) {
        if (BuildConfig.DEBUG) {
            super.w(t, format, args);
        }
    }

    @Override
    public void w(Object obj) {
        if (BuildConfig.DEBUG) {
            super.w(obj);
        }
    }

    @Override
    public void e(@NonNull String format, Object... args) {
        if (BuildConfig.DEBUG) {
            super.e(format, args);
        }
    }

    @Override
    public void e(Throwable t, @NonNull String format, Object... args) {
        if (BuildConfig.DEBUG) {
            super.e(t, format, args);
        }
    }

    @Override
    public void e(Object obj) {
        if (BuildConfig.DEBUG) {
            super.w(obj);
        }
    }

    @Override
    public void log(int priority, @NonNull String message, Throwable t) {
        if (BuildConfig.DEBUG) {
            super.log(priority, message, t);
        }
    }
}
