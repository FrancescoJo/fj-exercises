/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.ui.common;

import android.view.View;
import android.widget.TextView;

import com.fj.android.mediamonkey.R;
import com.fj.android.mediamonkey.dto.PluginManifestDto;
import com.mediamonkey.android.plugin.PluginAccessHelper;

import butterknife.ButterKnife;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 28 - Nov - 2016
 */
public class DrawerMenuTopViewHolder {
    private final View     rootView;
    private final TextView tvPluginName;
    private final TextView tvPluginVer;

    public DrawerMenuTopViewHolder(final View rootView) {
        this.rootView = rootView;
        this.tvPluginName = ButterKnife.findById(rootView, R.id.txt_menu_main_top_plugin_name);
        this.tvPluginVer  = ButterKnife.findById(rootView, R.id.txt_menu_main_top_plugin_ver);
    }

    public void draw(final PluginAccessHelper pluginHelper) {
        if (null == pluginHelper || !pluginHelper.isPluginLoaded()) {
            rootView.setVisibility(View.GONE);
            return;
        }

        rootView.setVisibility(View.VISIBLE);
        PluginManifestDto manifest = pluginHelper.getManifest();
        tvPluginName.setText(manifest.getName());
        tvPluginVer.setText(manifest.getVersion().toString());
    }
}
