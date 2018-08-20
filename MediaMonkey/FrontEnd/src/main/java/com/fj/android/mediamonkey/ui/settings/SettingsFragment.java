/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.fj.android.mediamonkey.R;
import com.fj.android.mediamonkey.util.lang.CastUtils;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 12 - Jan - 2017
 */
public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
    private ListPreference prefViewerMode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Context context = getActivity().getApplicationContext();
        PreferenceManager.setDefaultValues(context, R.xml.preferences, false);

        this.prefViewerMode = (ListPreference) getPreferenceManager().findPreference(context.getString(R.string.pref_key_viewer_mode));
        prefViewerMode.setOnPreferenceChangeListener(this);

        SharedPreferences prefs = getPreferenceManager().getSharedPreferences();
        onPreferenceChange(prefViewerMode, prefs.getString(prefViewerMode.getKey(), ""));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onPreferenceChange(final Preference preference, final Object newValue) {
        if (preference.getKey().equals(preference.getKey())) {
            int index = CastUtils.parseInt(newValue, 0);
            String valueEntry = getResources().getStringArray(R.array.viewer_mode_entries)[index];
            prefViewerMode.setSummary(valueEntry);
            prefViewerMode.setValue(String.valueOf(index));
        }
        return false;
    }
}
