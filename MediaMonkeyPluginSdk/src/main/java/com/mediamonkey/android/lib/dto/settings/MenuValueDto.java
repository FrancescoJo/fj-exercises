/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.lib.dto.settings;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 01 - Dec - 2016
 */
public class MenuValueDto<T> {
    private ValueType type;
    private T         value;

    public MenuValueDto(ValueType type, T value) {
        this.type  = type;
        this.value = value;
    }

    public final ValueType getType() {
        return type;
    }

    public final T getValue() {
        return type.cast(value);
    }

    public final void setValue(T value) {
        this.value = value;
    }

    public enum ValueType {
        INTEGER(Integer.class),
        STRING(String.class),
        DOUBLE(Double.class),
        BOOLEAN(Boolean.class);

        private Class<?> typeClass;

        ValueType(Class<?> klass) {
            this.typeClass = klass;
        }

        <T> T cast(Object value) {
            return (T) typeClass.cast(value);
        }
    }
}
