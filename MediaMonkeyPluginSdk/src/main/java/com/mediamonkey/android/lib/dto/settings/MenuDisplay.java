/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.lib.dto.settings;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 01 - Dec - 2016
 */
public interface MenuDisplay {
    @NonNull
    CharSequence getTitle();

    @Nullable
    CharSequence getDescription();
}
