/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.lib.dto.settings;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 01 - Dec - 2016
 */
public final class MenuGroupDto implements MenuDisplay {
    private CharSequence      title;
    private List<MenuItemDto> menuItems;
    private CharSequence      description;

    public final List<MenuItemDto> getMenuItems() {
        return menuItems;
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

    public static class Builder {
        private CharSequence      title;
        private List<MenuItemDto> menuItems = new ArrayList<>();
        private CharSequence      description;

        public Builder title(@NonNull CharSequence title) {
            this.title = title;
            return this;
        }

        public Builder addItem(@NonNull MenuItemDto menuItem) {
            menuItems.add(menuItem);
            return this;
        }

        public Builder description(@Nullable CharSequence description) {
            this.description = description;
            return this;
        }

        public MenuGroupDto create() {
            MenuGroupDto dto = new MenuGroupDto();
            dto.title        = this.title;
            dto.menuItems    = Collections.unmodifiableList(this.menuItems);
            dto.description  = this.description;
            return dto;
        }
    }

    private MenuGroupDto() { }
}
