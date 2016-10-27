/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.fj.android.template.util.logging;

import android.support.annotation.NonNull;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 01 - Sep - 2016
 */
@SuppressWarnings("unused")
public interface Logger {
    void v(final @NonNull String format, final Object... args);

    void v(final Throwable t, final @NonNull String format, final Object... args);

    void v(final Object obj);

    void d(final @NonNull String format, final Object... args);

    void d(final Throwable t, final @NonNull String format, final Object... args);

    void d(final Object obj);

    void i(final @NonNull String format, final Object... args);

    void i(final Throwable t, final @NonNull String format, final Object... args);

    void i(final Object obj);

    void w(final @NonNull String format, final Object... args);

    void w(final Throwable t, final @NonNull String format, final Object... args);

    void w(final Object obj);

    void e(final @NonNull String format, final Object... args);

    void e(final Throwable t, final @NonNull String format, final Object... args);

    void e(final Object obj);

    void log(final int priority, final @NonNull String message, final Throwable t);
}
