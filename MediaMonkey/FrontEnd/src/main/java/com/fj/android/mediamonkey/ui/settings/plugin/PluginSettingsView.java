/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.ui.settings.plugin;

import android.content.Context;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 29 - Nov - 2016
 */
interface PluginSettingsView {
    void showLoading();

    void hideLoading();

    void exit();

    void onAdapterPopulated(SettingsListAdapter adapter);

    Context getContext();
}
