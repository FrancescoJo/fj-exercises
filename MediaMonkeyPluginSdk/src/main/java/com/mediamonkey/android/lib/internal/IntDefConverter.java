/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.lib.internal;

import com.mediamonkey.android.lib.annotation.DisplayType;

/**
 * Hope we can use default method in Java interface soon.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 25 - Nov - 2016
 */
public final class IntDefConverter {
    public static @DisplayType int toDisplayType(int value) {
        if (DisplayType.GRID == value) {
            return DisplayType.GRID;
        } else if (DisplayType.LIST == value) {
            return DisplayType.LIST;
        } else {
            return DisplayType.LIST;
        }
    }

    private IntDefConverter() { }
}
