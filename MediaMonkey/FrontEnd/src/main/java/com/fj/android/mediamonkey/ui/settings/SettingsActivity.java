/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.ui.settings;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.fj.android.mediamonkey.R;
import com.fj.android.mediamonkey.ui._base.AbstractBaseActivity;
import com.fj.android.mediamonkey.ui._base.BasePresenter;
import com.fj.android.mediamonkey.util.view.ActionBarUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 12 - Jan - 2017
 */
public class SettingsActivity extends AbstractBaseActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected List<? extends BasePresenter> getPresenters() {
        return null;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getActivityComponent().inject(this);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        ActionBarUtils.setDisplayHomeAsUpEnabled(getSupportActionBar(), true);
        ActionBarUtils.setDisplayShowHomeEnabled(getSupportActionBar(), true);

        getFragmentManager().beginTransaction()
                .add(R.id.layout_settings_content,
                        Fragment.instantiate(this, SettingsFragment.class.getCanonicalName()))
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Home as up (back)
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
