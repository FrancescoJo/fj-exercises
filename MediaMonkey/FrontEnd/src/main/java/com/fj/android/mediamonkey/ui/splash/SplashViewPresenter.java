/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.ui.splash;

import com.fj.android.mediamonkey.annotation.PersistOnConfigChange;
import com.fj.android.mediamonkey.inject.objects.SharedPrefManager;
import com.fj.android.mediamonkey.plugin.PluginManager;
import com.fj.android.mediamonkey.ui._base.BasePresenter;
import com.mediamonkey.android.plugin.PluginAccessHelper;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 18 - Oct - 2016
 */
@PersistOnConfigChange
class SplashViewPresenter extends BasePresenter<SplashView> {
    @Inject SharedPrefManager prefMgr;
    @Inject PluginManager     pluginMgr;

    @Inject
    SplashViewPresenter() { }

    void loadPlugin(Observable<?> untilWhen) {
        getView().showLoading();
        pluginMgr.loadLastUsedPlugin()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .takeUntil(untilWhen)
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        getView().hideLoading();
                    }
                })
                .subscribe(new Consumer<PluginAccessHelper>() {
                    @Override
                    public void accept(PluginAccessHelper pluginAccessHelper) throws Exception {
                        getView().proceedToMain();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getView().proceedToMain();
                    }
                });
    }
}
