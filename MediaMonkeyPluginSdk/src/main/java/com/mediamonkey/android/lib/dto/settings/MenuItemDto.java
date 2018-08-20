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
public class MenuItemDto implements MenuDisplay {
    private final CharSequence title;
    private final CharSequence description;

    public MenuItemDto(@NonNull CharSequence title, @Nullable CharSequence description) {
        this.title       = title;
        this.description = description;
    }

    @NonNull
    @Override
    public final CharSequence getTitle() {
        return title;
    }

    @Nullable
    @Override
    public final CharSequence getDescription() {
        return description;
    }
}
