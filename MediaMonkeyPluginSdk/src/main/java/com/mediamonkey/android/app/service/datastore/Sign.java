/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.app.service.datastore;

/**
 * Sign specifier which is used in {@link com.mediamonkey.android.app.service.datastore.Where} condition object.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Dec - 2016
 */
public enum Sign {
    EQ("="),
    NE("!="),
    GT(">"),
    LT("<"),
    GTE(">="),
    LTE("<=");

    final String value;

    Sign(final String sign) {
        this.value = sign;
    }
}