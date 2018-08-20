/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.event;

import com.fj.android.mediamonkey.util.event.Event;
import com.mediamonkey.android.lib.dto.settings.MenuItemDto;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 29 - Nov - 2016
 */
public class PluginSettingsUpdateEvent implements Event {
    private final MenuItemDto menuItem;

    public PluginSettingsUpdateEvent(MenuItemDto menuItem) {
        this.menuItem = menuItem;
    }

    @Override
    public String getName() {
        return PluginSettingsUpdateEvent.class.getName();
    }

    public MenuItemDto getMenuItem() {
        return menuItem;
    }
}
