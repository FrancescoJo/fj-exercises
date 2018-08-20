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
 * @since 06 - Dec - 2016
 */
@IntDef({
    ContentDisplayType.UNDEFINED,
    ContentDisplayType.VERTICAL,
    ContentDisplayType.VERTICAL_PAGING,
    ContentDisplayType.HORIZONTAL
})
@Retention(RetentionPolicy.SOURCE)
public @interface ContentDisplayType {
    int UNDEFINED          = Integer.MIN_VALUE;

    int VERTICAL           = 0;

    int VERTICAL_PAGING    = 1;

    int HORIZONTAL         = 2;
}
