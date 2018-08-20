/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.ui.common;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;

import com.fj.android.mediamonkey.R;
import com.fj.android.mediamonkey.ui.CustomContentActivity;
import com.fj.android.mediamonkey.ui.EngineerModeFragment;
import com.fj.android.mediamonkey.ui.settings.SettingsActivity;
import com.fj.android.mediamonkey.ui.webview.CustomWebViewActivity;
import com.fj.android.mediamonkey.util.ResourceUtils;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jan - 2017
 */
class DrawerMenuEventHandlerImpl {
    static void onOptionsItemSelected(final Activity menuActivity, final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_drawer_app_engineer:
                CustomContentActivity.start(menuActivity, EngineerModeFragment.class);
                break;
            case R.id.menu_drawer_plugin_info:
                break;
            case R.id.menu_drawer_app_settings:
                menuActivity.startActivity(new Intent(menuActivity, SettingsActivity.class));
                break;
            case R.id.menu_drawer_help_oss:
                menuActivity.startActivity(CustomWebViewActivity.newLaunchIntent(menuActivity,
                        ResourceUtils.getString(R.string.menu_help_oss), ResourceUtils.getString(R.string.str_oss_file_uri)));
                break;
            case R.id.menu_drawer_help_about:
                break;
        }
    }

    private DrawerMenuEventHandlerImpl() { }
}
