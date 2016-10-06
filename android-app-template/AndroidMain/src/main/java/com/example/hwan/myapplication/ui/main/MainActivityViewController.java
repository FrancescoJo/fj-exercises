/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.example.hwan.myapplication.ui.main;

import com.example.hwan.myapplication.MyApplication;
import com.example.hwan.myapplication.annotation.PersistOnConfigChange;
import com.example.hwan.myapplication.api.ApiService;
import com.example.hwan.myapplication.dto.CounterDto;
import com.example.hwan.myapplication.ui.base.AbstractBaseActivity;
import com.example.hwan.myapplication.ui.base.BaseViewController;
import com.example.hwan.myapplication.util.RxUtils;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 18 - Oct - 2016
 */
@PersistOnConfigChange
class MainActivityViewController extends BaseViewController<MainActivityView> {
    private final ApiService apiService;

    @Inject
    /*default*/ MainActivityViewController() {
        this.apiService = MyApplication.getInstance().getAppComponent().apiService();
    }

    /*default*/ void getCounter() {
        apiService.getCounter()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .takeUntil(getView().getObservableLifecycle(MainActivity.LifeCycle.ON_DESTROY))
                .subscribe(new Action1<CounterDto>() {
                    @Override
                    public void call(final CounterDto counterDto) {
                        getView().showCounterResult(counterDto);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(final Throwable exception) {
                        getView().showCounterError(exception);
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        System.out.println("onCompleted");
                    }
                });
    }
}
