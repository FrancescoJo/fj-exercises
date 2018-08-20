/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.ui.webview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.fj.android.mediamonkey.R;
import com.fj.android.mediamonkey.ui._base.AbstractBaseActivity;
import com.fj.android.mediamonkey.ui._base.BasePresenter;
import com.fj.android.mediamonkey.util.view.ActionBarUtils;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 28 - Nov - 2016
 */
public class CustomWebViewActivity extends AbstractBaseActivity implements CustomWebView {
    private static final String KEY_DEFAULT_URI = "kDefUri";
    private static final String KEY_TITLE       = "kTItle";

    @Inject CustomWebViewPresenter presenter;

    @BindView(R.id.toolbar)             Toolbar toolbar;
    @BindView(R.id.view_webview_custom) WebView webView;

    @Override
    protected List<? extends BasePresenter> getPresenters() {
        return Collections.singletonList(presenter);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_webview);
        getActivityComponent().inject(this);
        ButterKnife.bind(this);
        presenter.attachView(this);
        setSupportActionBar(toolbar);

        ActionBarUtils.setDisplayHomeAsUpEnabled(getSupportActionBar(), true);
        ActionBarUtils.setDisplayShowHomeEnabled(getSupportActionBar(), true);

        WebSettings webSetting = webView.getSettings();
        webSetting.setBuiltInZoomControls(true);
//        webSetting.setJavaScriptEnabled(true);
        webSetting.setUseWideViewPort(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setInitialScale(1);

        String title = getIntent().getStringExtra(KEY_TITLE);
        String browsingUrl = getIntent().getStringExtra(KEY_DEFAULT_URI);
        ActionBarUtils.setTitle(getSupportActionBar(), title);
        ActionBarUtils.setTitleMarquee(toolbar);

        webView.loadUrl(browsingUrl);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    public static Intent newLaunchIntent(Context callerContext, String title, String defaultUrl) {
        Intent intent = new Intent(callerContext, CustomWebViewActivity.class);
        intent.putExtra(KEY_DEFAULT_URI, defaultUrl);
        intent.putExtra(KEY_TITLE, title);
        return intent;
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
