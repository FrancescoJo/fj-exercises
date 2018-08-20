/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.dao.sharedpref;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fj.android.mediamonkey.R;
import com.fj.android.mediamonkey.util.IntDefUtils;
import com.fj.android.mediamonkey.util.ResourceUtils;
import com.mediamonkey.android.lib.annotation.ContentDisplayType;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 23 - Nov - 2016
 */
public class AppConfigPref {
    private SharedPreferences pref;
    private static final String KEY_LAST_USED_PLUGIN_PKG = "kLastUsedPluginPkg";
    private static final String KEY_DB_CREATED           = "kDbCreated:";

    public AppConfigPref(final Context context) {
        this.pref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getLastUsedPluginPackage() {
        return pref.getString(KEY_LAST_USED_PLUGIN_PKG, "");
    }

    public void setLastUsedPluginPackage(String packageName) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY_LAST_USED_PLUGIN_PKG, packageName);
        editor.apply();
    }

    public boolean isDatabaseCreated(String dbName) {
        return pref.getBoolean(KEY_DB_CREATED + dbName, false);
    }

    public void setDatabaseCreated(String dbName) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(KEY_DB_CREATED + dbName, true);
        editor.apply();
    }

    public @ContentDisplayType int getViewerMode() {
        String keyName = ResourceUtils.getString(R.string.pref_key_viewer_mode);
        return IntDefUtils.Str2ContentDisplayType(pref.getString(keyName, "0"), ContentDisplayType.VERTICAL);
    }
}
