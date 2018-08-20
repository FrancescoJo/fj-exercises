/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.inject.component;

import com.fj.android.mediamonkey.inject.module.ActivityModule;
import com.fj.android.mediamonkey.ui.filepicker.FilePickerActivity;
import com.fj.android.mediamonkey.ui.main.MainActivity;
import com.fj.android.mediamonkey.ui.settings.SettingsActivity;
import com.fj.android.mediamonkey.ui.settings.plugin.PluginSettingsActivity;
import com.fj.android.mediamonkey.ui.splash.SplashActivity;
import com.fj.android.mediamonkey.ui.viewer.ViewerActivity;
import com.fj.android.mediamonkey.ui.webview.CustomWebViewActivity;

import dagger.Subcomponent;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 18 - Oct - 2016
 */
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(SplashActivity activity);

    void inject(MainActivity activity);

    void inject(FilePickerActivity activity);

    void inject(CustomWebViewActivity activity);

    void inject(PluginSettingsActivity activity);

    void inject(SettingsActivity activity);

    void inject(ViewerActivity activity);
}
