/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.ui.settings.plugin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.fj.android.mediamonkey.R;
import com.fj.android.mediamonkey.dto.PluginManifestDto;
import com.fj.android.mediamonkey.event.PluginSettingsUpdateEvent;
import com.fj.android.mediamonkey.event.RequestPluginSettingsActivityContextEvent;
import com.fj.android.mediamonkey.event.ResponsePluginSettingsActivityContextEvent;
import com.fj.android.mediamonkey.inject.objects.RxEventBus;
import com.fj.android.mediamonkey.ui._base.AbstractBaseActivity;
import com.fj.android.mediamonkey.ui._base.ActivityLifecycle;
import com.fj.android.mediamonkey.ui._base.BasePresenter;
import com.fj.android.mediamonkey.util.ResourceUtils;
import com.fj.android.mediamonkey.util.RxUtils;
import com.fj.android.mediamonkey.util.view.ActionBarUtils;
import com.mediamonkey.android.lib.dto.settings.MenuItemDto;
import com.mediamonkey.android.plugin.PluginAccessHelper;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 29 - Nov - 2016
 */
public class PluginSettingsActivity extends AbstractBaseActivity implements PluginSettingsView {
    @Inject PluginSettingsPresenter presenter;
    @Inject PluginAccessHelper      pluginHelper;
    @Inject RxEventBus              eventBus;

    @BindView(R.id.toolbar)                        Toolbar      toolbar;
    @BindView(R.id.list_plugin_settings)           RecyclerView listSettings;
    @BindView(R.id.layout_loading_plugin_settings) View         loadingView;
    private SettingsListAdapter                                 listAdapter;

    private Disposable pluginSettingsUpdateEventReceiver;
    private Disposable contextRequestEventReceiver;
    private boolean    isSettingUpdated;

    @Override
    protected List<? extends BasePresenter> getPresenters() {
        return Collections.singletonList(presenter);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin_settings);
        getActivityComponent().inject(this);
        ButterKnife.bind(this);
        presenter.attachView(this);
        setSupportActionBar(toolbar);

        PluginManifestDto manifest = pluginHelper.getManifest();
        ActionBarUtils.setTitle(getSupportActionBar(), ResourceUtils.getString(R.string.text_title_plugin_settings, manifest.getName()));
        ActionBarUtils.setTitleMarquee(toolbar);

        LinearLayoutManager llMgr = new LinearLayoutManager(this);
        listSettings.setLayoutManager(llMgr);

        presenter.populateMenu(pluginHelper.getPluginInstance().getSettingsService(),
                getObservableLifeCycle(ActivityLifecycle.ON_DESTROY));
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.pluginSettingsUpdateEventReceiver = eventBus.eventByType(PluginSettingsUpdateEvent.class)
                .subscribe(new Consumer<PluginSettingsUpdateEvent>() {
                    @Override
                    public void accept(PluginSettingsUpdateEvent event) throws Exception {
                        isSettingUpdated = true;
                        onSettingUpdated(event.getMenuItem());
                    }
                });
        this.contextRequestEventReceiver = eventBus.eventByType(RequestPluginSettingsActivityContextEvent.class)
                .subscribe(new Consumer<RequestPluginSettingsActivityContextEvent>() {
                    @Override
                    public void accept(RequestPluginSettingsActivityContextEvent requestPluginSettingsActivityContextEvent) throws Exception {
                        eventBus.post(new ResponsePluginSettingsActivityContextEvent(PluginSettingsActivity.this));
                    }
                });
    }

    @Override
    protected void onStop() {
        RxUtils.dispose(pluginSettingsUpdateEventReceiver);
        RxUtils.dispose(contextRequestEventReceiver);
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (isSettingUpdated) {
            setResult(RESULT_OK);
        } else {
            setResult(RESULT_CANCELED);
        }

        super.onBackPressed();
    }

    private void onSettingUpdated(MenuItemDto menuItem) {
        listAdapter.notifyItemUpdated(menuItem);
    }

    public static Intent newLaunchIntent(Context callerContext) {
        return new Intent(callerContext, PluginSettingsActivity.class);
    }

    @Override
    public void showLoading() {
        listSettings.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        listSettings.setVisibility(View.VISIBLE);
        loadingView.setVisibility(View.GONE);
    }

    @Override
    public void exit() {
        finish();
    }

    @Override
    public void onAdapterPopulated(SettingsListAdapter adapter) {
        this.listAdapter = adapter;
        listSettings.setAdapter(adapter);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
