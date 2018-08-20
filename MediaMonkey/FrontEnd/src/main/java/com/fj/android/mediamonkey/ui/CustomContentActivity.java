/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.fj.android.mediamonkey.R;
import com.fj.android.mediamonkey.util.view.ActionBarUtils;

import butterknife.ButterKnife;

/**
 * Custom fragment injectable Activity.
 * Mainly used for back door features only exist on DEBUG build phase.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 15 - Jan - 2017
 */
public class CustomContentActivity extends AppCompatActivity {
    private static final String KEY_FRAG_NAME = "kFrgName";

    @Override
    protected void onCreate(final @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_content);
        Toolbar toolbar              = ButterKnife.findById(this, R.id.toolbar);
        RelativeLayout contentLayout = ButterKnife.findById(this, R.id.layout_custom_content);
        setSupportActionBar(toolbar);

        ActionBarUtils.setDisplayHomeAsUpEnabled(getSupportActionBar(), true);
        ActionBarUtils.setDisplayShowHomeEnabled(getSupportActionBar(), true);

        String canonicalClassName = getIntent().getStringExtra(KEY_FRAG_NAME);
        getFragmentManager().beginTransaction()
                .add(contentLayout.getId(), Fragment.instantiate(this, canonicalClassName))
                .commit();
    }

    public static void start(final Activity callerActivity,
                             final Class<? extends Fragment> fragmentClass) {
        Intent intent = new Intent(callerActivity, CustomContentActivity.class);
        intent.putExtra(KEY_FRAG_NAME, fragmentClass.getCanonicalName());
        callerActivity.startActivity(intent);
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
