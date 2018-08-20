/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.plugin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.mediamonkey.android.app.MediaMonkeyApplicationDelegate;
import com.mediamonkey.android.lib.MediaMonkeyPlugin;
import com.mediamonkey.android.lib.service.ContentsService;
import com.mediamonkey.android.lib.service.PluginSettingsService;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 25 - Nov - 2016
 */
public class PluginImpl extends MediaMonkeyPlugin {
    @WorkerThread
    @Override
    protected void onCreate(Context applicationContext, MediaMonkeyApplicationDelegate appDelegate) {
        System.out.println("PluginImpl onCreate: " + applicationContext);
    }

    @WorkerThread
    protected void onDestroy() {
        System.out.println("PluginImpl onDestroy");
    }

    @Override
    @NonNull
    public ContentsService getTableOfContentsService() {
        return new ContentsServiceImpl();
    }

    @Nullable
    @Override
    public PluginSettingsService getSettingsService() {
        return null;
    }
}
