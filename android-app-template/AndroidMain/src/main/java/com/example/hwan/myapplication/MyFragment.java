/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.example.hwan.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hwan.myapplication.util.logging.LogFactory;
import com.example.hwan.myapplication.util.logging.Logger;

import rx.Observable;
import rx.functions.Func1;
import rx.subjects.BehaviorSubject;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 01 - Sep - 2016
 */
public class MyFragment extends Fragment {
    @IntDef({
            LifeCycle.BEFORE_ON_ATTACH,
            LifeCycle.ON_ATTACH,
            LifeCycle.ON_CREATE,
            LifeCycle.ON_CREATE_VIEW,
            LifeCycle.ON_ACTIVITY_CREATED,
            LifeCycle.ON_START,
            LifeCycle.ON_RESUME,
            LifeCycle.ON_PAUSE,
            LifeCycle.ON_STOP,
            LifeCycle.ON_DESTROY_VIEW,
            LifeCycle.ON_DESTROY,
            LifeCycle.ON_DETACH
    })
    public @interface LifeCycle {
        int BEFORE_ON_ATTACH    = 0;
        int ON_ATTACH           = 1;
        int ON_CREATE           = 2;
        int ON_CREATE_VIEW      = 3;
        int ON_ACTIVITY_CREATED = 4;
        int ON_START            = 5;
        int ON_RESUME           = 6;
        int ON_PAUSE            = 7;
        int ON_STOP             = 8;
        int ON_DESTROY_VIEW     = 9;
        int ON_DESTROY          = 10;
        int ON_DETACH           = 11;
    }

    private @LifeCycle int currentLifecycle = LifeCycle.BEFORE_ON_ATTACH;
    private BehaviorSubject<Integer> lifecycleSubject;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        logLifecycle(String.format("onAttach:context %s", context));
        setCurrentLifecycle(LifeCycle.ON_ATTACH);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logLifecycle(String.format("onCreate:bundle %s", savedInstanceState));
        setCurrentLifecycle(LifeCycle.ON_CREATE);
    }

    @Nullable
    @Override
    @CallSuper
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        logLifecycle(String.format("onCreateView:container %s, bundle %s", container, savedInstanceState));
        setCurrentLifecycle(LifeCycle.ON_CREATE_VIEW);
        return null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        logLifecycle(String.format("onActivityCreated:bundle %s", savedInstanceState));
        setCurrentLifecycle(LifeCycle.ON_ACTIVITY_CREATED);
    }

    @Override
    public void onStart() {
        super.onStart();
        logLifecycle("onStart");
        setCurrentLifecycle(LifeCycle.ON_START);
    }

    @Override
    public void onResume() {
        super.onResume();
        logLifecycle("onResume");
        setCurrentLifecycle(LifeCycle.ON_RESUME);
    }

    @Override
    public void onPause() {
        super.onPause();
        logLifecycle("onPause");
        setCurrentLifecycle(LifeCycle.ON_PAUSE);
    }

    @Override
    public void onStop() {
        super.onStop();
        logLifecycle("onStop");
        setCurrentLifecycle(LifeCycle.ON_STOP);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        logLifecycle("onDestroyView");
        setCurrentLifecycle(LifeCycle.ON_DESTROY_VIEW);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        logLifecycle("onDestroy");
        setCurrentLifecycle(LifeCycle.ON_DESTROY);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        logLifecycle("onDetach");
        setCurrentLifecycle(LifeCycle.ON_DETACH);
    }

    /**
     * This method will be convenient when managing lifecycle of
     * <a href="http://reactivex.io/documentation/observable.html">Observable</a> subscription using
     * <a href="http://reactivex.io/documentation/operators/takeuntil.html">takeUntil</a> operator.
     * <p>
     * Using <code>takeUntil(Lifecycle)</code> idiom will help us to forget to calling <code>Observable#unsubscribe</code>
     * to avoid memory leak on fragment when subscribed <code>Observable</code> lives longer than fragment.
     * </p>
     *
     * @return An observable of fragment lifecycle state.
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
                case LifeCycle.BEFORE_ON_ATTACH:
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
