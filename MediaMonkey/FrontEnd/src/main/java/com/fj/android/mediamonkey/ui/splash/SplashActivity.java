/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fj.android.mediamonkey.MediaMonkeyApplication;
import com.fj.android.mediamonkey.R;
import com.fj.android.mediamonkey.dao.sharedpref.AppConfigPref;
import com.fj.android.mediamonkey.ui._base.AbstractBaseActivity;
import com.fj.android.mediamonkey.ui._base.ActivityLifecycle;
import com.fj.android.mediamonkey.ui._base.BasePresenter;
import com.fj.android.mediamonkey.ui.main.MainActivity;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Aug - 2016
 */
public class SplashActivity extends AbstractBaseActivity implements SplashView {
    @Inject SplashViewPresenter presenter;

    @BindView(R.id.layout_loading_splash) View     loadingView;

    @Override
    protected List<? extends BasePresenter> getPresenters() {
        return Collections.singletonList(presenter);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getActivityComponent().inject(this);
        ButterKnife.bind(this);
        presenter.attachView(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.loadPlugin(getObservableLifeCycle(ActivityLifecycle.ON_DESTROY));
    }

    @Override
    public void showLoading() {
        this.loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        this.loadingView.setVisibility(View.GONE);
    }

    @Override
    public void proceedToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }
}
