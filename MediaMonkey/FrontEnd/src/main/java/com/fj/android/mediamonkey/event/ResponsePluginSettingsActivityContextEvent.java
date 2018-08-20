/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.event;

import android.content.Context;
import android.support.annotation.Nullable;

import com.fj.android.mediamonkey.ui.settings.plugin.PluginSettingsActivity;
import com.fj.android.mediamonkey.util.event.Event;

import java.lang.ref.WeakReference;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 03 - Dec - 2016
 */
public class ResponsePluginSettingsActivityContextEvent implements Event {
    private WeakReference<PluginSettingsActivity> contextRef;

    public ResponsePluginSettingsActivityContextEvent(PluginSettingsActivity context) {
        this.contextRef = new WeakReference<>(context);
    }

    @Override
    public String getName() {
        return ResponsePluginSettingsActivityContextEvent.class.getSimpleName();
    }

    @Nullable
    public Context getContext() {
        return contextRef.get();
    }
}
