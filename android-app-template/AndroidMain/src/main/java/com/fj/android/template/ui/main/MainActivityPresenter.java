/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.fj.android.template.ui.main;

import com.fj.android.template.MyApplication;
import com.fj.android.template.annotation.PersistOnConfigChange;
import com.fj.android.template.api.ApiService;
import com.fj.android.template.dto.CounterDto;
import com.fj.android.template.ui.base.AutoDisposablePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 18 - Oct - 2016
 */
@PersistOnConfigChange
class MainActivityPresenter extends AutoDisposablePresenter<MainActivityView> {
    private final ApiService apiService;

    @Inject
    /*default*/ MainActivityPresenter() {
        this.apiService = MyApplication.getInstance().getAppComponent().apiService();
    }

    /*default*/ void displayCounter() {
        addToAutoDisposables(apiService.getCounter()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CounterDto>() {
                    @Override
                    public void accept(final CounterDto counterDto) {
                        getView().showCounterResult(counterDto);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(final Throwable exception) {
                        getView().showCounterError(exception);
                    }
                }));
    }
}
