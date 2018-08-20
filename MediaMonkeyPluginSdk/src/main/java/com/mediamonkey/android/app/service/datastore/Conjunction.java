package com.mediamonkey.android.app.service.datastore;

/**
 * Conditional specifier for mathematical evaluation,
 * which is used in {@link com.mediamonkey.android.app.service.datastore.Where} condition object.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Dec - 2016
 */
public enum Conjunction {
    AND("AND"),
    OR("OR");

    final String value;

    Conjunction(final String str) {
        this.value = str;
    }
}
