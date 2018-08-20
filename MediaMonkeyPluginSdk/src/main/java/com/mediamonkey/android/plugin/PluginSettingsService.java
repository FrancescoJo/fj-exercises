/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.plugin;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;

import com.mediamonkey.android.lib.dto.settings.MenuDisplay;
import com.mediamonkey.android.lib.dto.settings.MenuItemDto;
import com.mediamonkey.android.lib.dto.settings.MenuValueDto;

import java.util.List;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 29 - Nov - 2016
 */
public interface PluginSettingsService extends ShutdownableService {
    @NonNull
    List<MenuDisplay> getMenus();

    @UiThread
    void onMenuSelected(MenuItemDto menuItem);

    @UiThread
    boolean isEnabled(MenuDisplay menuDisplay);

    @Nullable
    MenuValueDto getMenuValueOf(MenuItemDto menuItem);
}
