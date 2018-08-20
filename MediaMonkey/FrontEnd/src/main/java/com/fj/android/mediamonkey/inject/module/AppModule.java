/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.inject.module;

import com.fj.android.mediamonkey.MediaMonkeyApplication;
import com.fj.android.mediamonkey.inject.objects.SharedPrefManager;
import com.fj.android.mediamonkey.plugin.PluginManager;
import com.mediamonkey.android.plugin.PluginAccessHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Aug - 2016
 */
@Module
public final class AppModule {
    @Provides
    @Singleton
    SharedPrefManager providesSharePrefMgr() {
        return new SharedPrefManager(MediaMonkeyApplication.getInstance());
    }

    @Provides
    @Singleton
    PluginManager providesPluginMgr() {
        return new PluginManager(MediaMonkeyApplication.getInstance());
    }

    @Provides
    @Singleton
    PluginAccessHelper providesPluginAccessHelper() {
        return new PluginAccessHelper();
    }
}
