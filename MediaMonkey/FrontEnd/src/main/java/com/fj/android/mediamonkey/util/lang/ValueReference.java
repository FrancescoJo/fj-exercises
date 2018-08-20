/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.util.lang;

/**
 * Value-referencing helper which is used for updating value outside of
 * anonymous inner class. Not recommended to use this as <code>static final</code>.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 16 - Dec - 2016
 */
public class ValueReference<T> {
    private T value;

    public ValueReference() {
        this(null);
    }

    public ValueReference(T initialValue) {
        this.value = initialValue;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }

    /**
     * @return a string representation of referencing value if value is set, empty String otherwise.
     */
    @Override
    public String toString() {
        return null != value ? value.toString() : "";
    }
}
