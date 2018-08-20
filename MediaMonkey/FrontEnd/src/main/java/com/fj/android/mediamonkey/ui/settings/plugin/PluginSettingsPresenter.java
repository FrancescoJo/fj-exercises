/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.ui.settings.plugin;

import com.fj.android.mediamonkey.annotation.PersistOnConfigChange;
import com.fj.android.mediamonkey.plugin.PluginImplException;
import com.fj.android.mediamonkey.ui._base.BasePresenter;
import com.fj.android.mediamonkey.ui.common.DialogHelper;
import com.mediamonkey.android.lib.dto.settings.MenuItemDto;
import com.mediamonkey.android.plugin.PluginSettingsService;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 01 - Dec - 2016
 */
@PersistOnConfigChange
class PluginSettingsPresenter extends BasePresenter<PluginSettingsView> {
    private PluginSettingsService pluginSettingsService;

    @Inject
    PluginSettingsPresenter() { }

    void populateMenu(final PluginSettingsService pluginSettings,
                      final Observable<?> untilWhen) {
        this.pluginSettingsService = pluginSettings;
        getView().showLoading();
        Observable.create(new ObservableOnSubscribe<SettingsListAdapter>() {
            @Override
            public void subscribe(ObservableEmitter<SettingsListAdapter> emitter) throws Exception {
                try {
                    SettingsListAdapter adapter = new SettingsListAdapter(PluginSettingsPresenter.this, pluginSettings);
                    emitter.onNext(adapter);
                    emitter.onComplete();
                } catch (Exception e) {
                    throw new PluginImplException("Error while populating plugin settings menu");
                }
            }
        }).takeUntil(untilWhen).subscribe(new Consumer<SettingsListAdapter>() {
            @Override
            public void accept(SettingsListAdapter settingsListAdapter) throws Exception {
                getView().hideLoading();
                getView().onAdapterPopulated(settingsListAdapter);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                DialogHelper.showPluginException(getView().getContext(), throwable, new Runnable() {
                    @Override
                    public void run() {
                        getView().exit();
                    }
                });
            }
        });
    }

    @Override
    protected void onDetachView(@SuppressWarnings("UnusedParameters") boolean isConfigChanged) {
        this.pluginSettingsService = null;
        super.onDetachView(isConfigChanged);
    }

    void onClickMenuItem(MenuItemDto item) {
        if (null == pluginSettingsService) {
            Timber.w("Presenter is detached but click event received: %s(%s)", item, item.getTitle());
            return;
        }
        pluginSettingsService.onMenuSelected(item);
    }
}
