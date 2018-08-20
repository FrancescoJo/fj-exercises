/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.app.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.UiThread;

import com.fj.android.mediamonkey.MediaMonkeyApplication;
import com.fj.android.mediamonkey.dto.PluginManifestDto;
import com.fj.android.mediamonkey.event.PluginSettingsUpdateEvent;
import com.fj.android.mediamonkey.inject.objects.RxEventBus;
import com.mediamonkey.android.lib.dto.settings.MenuItemDto;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 29 - Nov - 2016
 */
public class SettingsServiceImpl implements SettingsService {
    private SharedPreferences prefs;
    private RxEventBus        eventBus;

    public SettingsServiceImpl(PluginManifestDto manifest, RxEventBus eventBus) {
        this.prefs   = MediaMonkeyApplication.getInstance()
                .getSharedPreferences(manifest.getPackageName(), Context.MODE_PRIVATE);
        this.eventBus = eventBus;
    }

    @UiThread
    @Override
    public void notifySettingUpdated(MenuItemDto menuItem) {
        eventBus.post(new PluginSettingsUpdateEvent(menuItem));
    }

    @Override
    public String readString(String key, String defaultValue) {
        return prefs.getString(key, defaultValue);
    }

    @Override
    public void writeString(String key, String value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public boolean readBoolean(String key, boolean defaultValue) {
        return prefs.getBoolean(key, defaultValue);
    }

    @Override
    public void writeBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    @Override
    public int readInt(String key, int defaultValue) {
        return prefs.getInt(key, defaultValue);
    }

    @Override
    public void writeInt(String key, int value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }
}
