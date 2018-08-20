/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.inject.component;

import com.fj.android.mediamonkey.inject.module.AppModule;
import com.fj.android.mediamonkey.inject.objects.RxEventBus;
import com.fj.android.mediamonkey.inject.objects.SharedPrefManager;
import com.fj.android.mediamonkey.plugin.PluginManager;
import com.mediamonkey.android.plugin.PluginAccessHelper;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Aug - 2016
 */
@Singleton
@Component(modules = { AppModule.class })
public interface AppComponent {
    RxEventBus eventBus();

    SharedPrefManager sharedPrefMgr();

    PluginManager pluginMgr();

    PluginAccessHelper pluginAccessHelper();
}
