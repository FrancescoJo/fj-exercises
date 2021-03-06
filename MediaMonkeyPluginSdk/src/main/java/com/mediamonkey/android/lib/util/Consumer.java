/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.lib.util;

/**
 * Same as {@link java.util.function.Consumer}, which is usable on API Level 24+.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 14 - Dec - 2016
 */
@SuppressWarnings("Since15")
public interface Consumer<T> {
    void accept(T argument);
}
