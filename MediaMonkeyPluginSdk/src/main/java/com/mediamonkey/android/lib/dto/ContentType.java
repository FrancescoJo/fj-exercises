/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.lib.dto;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 24 - Dec - 2016
 */
public enum ContentType {
    TEXT(0),
    RICH_TEXT(1),
    IMAGE(2),
    MUSIC(3),
    VIDEO(4);

    public final int value;

    ContentType(final int value) {
        this.value = value;
    }

    public static ContentType byValue(final int value) {
        for (ContentType type : ContentType.values()) {
            if (type.value == value) {
                return type;
            }
        }

        return TEXT;
    }
}
