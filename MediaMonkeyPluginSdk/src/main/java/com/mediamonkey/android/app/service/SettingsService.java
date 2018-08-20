/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.app.service;

import android.support.annotation.UiThread;

import com.mediamonkey.android.lib.dto.settings.MenuItemDto;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 29 - Nov - 2016
 */
public interface SettingsService {
    @UiThread
    void notifySettingUpdated(MenuItemDto menuItem);

    String readString(String key, String defaultValue);

    void writeString(String key, String value);

    boolean readBoolean(String key, boolean defaultValue);

    void writeBoolean(String key, boolean value);

    int readInt(String key, int defaultValue);

    void writeInt(String key, int value);
}
