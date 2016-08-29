/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.example.hwan.myapplication.util.logging;

import android.support.annotation.NonNull;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 01 - Sep - 2016
 */
@SuppressWarnings("unused")
public interface Logger {
    void v(@NonNull String format, Object... args);

    void v(Throwable t, @NonNull String format, Object... args);

    void v(Object obj);

    void d(@NonNull String format, Object... args);

    void d(Throwable t, @NonNull String format, Object... args);

    void d(Object obj);

    void i(@NonNull String format, Object... args);

    void i(Throwable t, @NonNull String format, Object... args);

    void i(Object obj);

    void w(@NonNull String format, Object... args);

    void w(Throwable t, @NonNull String format, Object... args);

    void w(Object obj);

    void e(@NonNull String format, Object... args);

    void e(Throwable t, @NonNull String format, Object... args);

    void e(Object obj);

    void log(int priority, @NonNull String message, Throwable t);
}
