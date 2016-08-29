/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.example.hwan.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.example.hwan.myapplication.util.logging.LogFactory;
import com.example.hwan.myapplication.util.logging.Logger;

import rx.Observable;
import rx.functions.Func1;
import rx.subjects.BehaviorSubject;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 01 - Sep - 2016
 */
@SuppressLint("Registered") // This file is working as 'base' activity.
public class MyActivity extends Activity {
    @IntDef({
            LifeCycle.BEFORE_ON_CREATE,
            LifeCycle.ON_CREATE,
            LifeCycle.ON_START,
            LifeCycle.ON_RESUME,
            LifeCycle.ON_PAUSE,
            LifeCycle.ON_STOP,
            LifeCycle.ON_DESTROY,
    })
    public @interface LifeCycle {
        int BEFORE_ON_CREATE = 0;
        int ON_CREATE        = 1;
        int ON_START         = 2;
        int ON_RESUME        = 3;
        int ON_PAUSE         = 4;
        int ON_STOP          = 5;
        int ON_DESTROY       = 6;
    }

    private @LifeCycle int currentLifecycle = LifeCycle.BEFORE_ON_CREATE;
    private BehaviorSubject<Integer> lifecycleSubject;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logLifecycle(String.format("onCreate:bundle %s", savedInstanceState));
        setCurrentLifecycle(LifeCycle.ON_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        logLifecycle("onStart");
        setCurrentLifecycle(LifeCycle.ON_START);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        logLifecycle("onRestart");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        logLifecycle(String.format("onRestoreInstanceState:bundle %s", savedInstanceState));
    }

    @Override
    protected void onResume() {
        super.onResume();
        logLifecycle("onResume");
        setCurrentLifecycle(LifeCycle.ON_RESUME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        logLifecycle("onPause");
        setCurrentLifecycle(LifeCycle.ON_PAUSE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        logLifecycle("onStop");
        setCurrentLifecycle(LifeCycle.ON_STOP);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logLifecycle("onDestroy");
        setCurrentLifecycle(LifeCycle.ON_DESTROY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        logLifecycle(String.format("onActivityResult: requestCode %s, resultCode %s", requestCode, resultCode));
        logLifecycle(String.format("onActivityResult: data %s", data));
    }

    /**
     * This method will be convenient when managing lifecycle of
     * <a href="http://reactivex.io/documentation/observable.html">Observable</a> subscription using
     * <a href="http://reactivex.io/documentation/operators/takeuntil.html">takeUntil</a> operator.
     * <p>
     * Using <code>takeUntil(Lifecycle)</code> idiom will help us to forget to calling <code>Observable#unsubscribe</code>
     * to avoid memory leak on activity when subscribed <code>Observable</code> lives longer than activity.
     * </p>
     *
     * @return An observable of activity lifecycle state.
     */
    protected Observable<Integer> getObservableLifecycle(final @LifeCycle int lifecycle) {
        return getObservableLifecycleInternal().filter(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(Integer l) {
                return l >= lifecycle;
            }
        }).take(1);
    }

    // Suppress SwitchIntDef: All cases are covered in 'default' clause
    @SuppressLint("SwitchIntDef")
    private Observable<Integer> getObservableLifecycleInternal() {
        if (null == lifecycleSubject) {
            switch (currentLifecycle) {
                case LifeCycle.BEFORE_ON_CREATE:
                    lifecycleSubject = BehaviorSubject.create();
                    break;
                case LifeCycle.ON_DESTROY:
                    return Observable.just(LifeCycle.ON_DESTROY);
                default:
                    lifecycleSubject = BehaviorSubject.create(currentLifecycle);
            }
        }
        return lifecycleSubject;
    }

    private void setCurrentLifecycle(@LifeCycle int lifecycle) {
        currentLifecycle = lifecycle;
        if (null != lifecycleSubject) {
            lifecycleSubject.onNext(lifecycle);
            if (lifecycle == LifeCycle.ON_DESTROY) {
                lifecycleSubject.onCompleted();
                lifecycleSubject = null;
            }
        }
    }

    private void logLifecycle(String lifecycle) {
        if (BuildConfig.DEBUG) {
            Logger log = LogFactory.getLogger(this.getClass().getSimpleName());
            log.v("entered lifecycle: %s", lifecycle);
        }
    }
}
