/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.lib.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 25 - Nov - 2016
 */
@IntDef({
    DisplayType.UNDEFINED,
    DisplayType.LIST,
    DisplayType.GRID,
    DisplayType.PAGE_HORIZONTAL,
    DisplayType.PAGE_VERTICAL
})
@Retention(RetentionPolicy.SOURCE)
public @interface DisplayType {
    int UNDEFINED = Integer.MIN_VALUE;

    int LIST            = 0;

    int GRID            = 1;

    int PAGE_HORIZONTAL = 2;

    int PAGE_VERTICAL   = 3;
}
