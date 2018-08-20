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

/**
 * Bridge class between MediaMonkey and Plugin implementations.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Nov - 2016
 */
public abstract class MediaMonkeyPlugin {
    public static final int DATASTORE_VERSION = 1;

    @WorkerThread
    protected MediaMonkeyPlugin() { }

    /**
     * Called when your plugin is loaded into memory.
     *
     * @param applicationContext    Application context of MediaMonkey frontend.
     */
    @WorkerThread
    protected void onCreate(Context applicationContext,
                            MediaMonkeyApplicationDelegate appDelegate) { }

    /**
     * Called when your plugin is unloaded from memory.
     * Note that all of memory claimed by your logic, must be freed at this point.
     */
    @WorkerThread
    protected void onDestroy() { }

    /**
     * Provides an instance of ContentsService class
     */
    @NonNull
    public abstract ContentsService getContentsService();

    /**
     * Provides an instance of plugin settings service.
     */
    @Nullable
    public abstract PluginSettingsService getSettingsService();

    /**
     * Provides an instance of DataStore interface.
     */
    @Nullable
    public abstract DataStoreInterface getDataStoreInterface();
}
