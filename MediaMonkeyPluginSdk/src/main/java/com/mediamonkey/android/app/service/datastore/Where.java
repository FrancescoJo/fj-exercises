/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.app.service.datastore;

import android.support.annotation.NonNull;

/**
 * Datastore range specifying object.
 *
 * @param <T> Any (boxed) Java primitive types or {@link java.util.Collection}s which
 *            wraps any Java primitive types.
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 09 - Dec - 2016
 */
public class Where<T> {
    static final Where<?> EMPTY = new Where<>("", null, null);

    final Conjunction conjunction;
    final String      lhs;
    final Sign        sign;
    final T           rhs;

    /**
     * Condition constructor which is used as single condition.
     */
    @SuppressWarnings("ConstantConditions")
    public Where(final String columnName, final Sign sign, final T value) {
        this(null, columnName, sign, value);
    }

    /**
     * Condition constructor which is used as reference of multiple conditions.
     *
     * @param conjunction Conjunction, must be provided if you are planning to use multiple conditions.
     * @param columnName  Key name of schema, usually a field name of {@link com.mediamonkey.android.lib.util.DataStorable} object.
     * @param sign        Range specifying sign.
     * @param value       Actual value to search in DataStore.
     */
    public Where(final @NonNull Conjunction conjunction, final String columnName, final Sign sign, final T value) {
        this.conjunction = conjunction;
        this.lhs = columnName;
        this.sign = sign;
        this.rhs = value;
    }
}