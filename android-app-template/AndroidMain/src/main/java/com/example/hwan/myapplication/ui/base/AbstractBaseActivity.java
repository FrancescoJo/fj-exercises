/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.example.hwan.myapplication.ui.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.example.hwan.myapplication.BuildConfig;
import com.example.hwan.myapplication.MyApplication;
import com.example.hwan.myapplication.inject.component.ActivityComponent;
import com.example.hwan.myapplication.inject.component.DaggerPersistOnConfigChangeComponent;
import com.example.hwan.myapplication.inject.component.PersistOnConfigChangeComponent;
import com.example.hwan.myapplication.inject.module.ActivityModule;
import com.example.hwan.myapplication.util.logging.LogFactory;
import com.example.hwan.myapplication.util.logging.Logger;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import rx.Observable;
import rx.functions.Func1;
import rx.subjects.BehaviorSubject;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 01 - Sep - 2016
 */
public abstract class AbstractBaseActivity extends Activity implements LifecycleObservableView {
    private static final String KEY_ACTIVITY_ID = AbstractBaseActivity.class.getCanonicalName() + ".KEY_ACTIVITY_ID";
    private static final AtomicInteger ID_HOLDER = new AtomicInteger(0);
    private static final SparseArray<PersistOnConfigChangeComponent> COMPONENTS_MAP = new SparseArray<>();

    private @LifeCycle int currentLifecycle = LifeCycle.BEFORE_ON_CREATE;
    private BehaviorSubject<Integer> lifecycleSubject;
    private ActivityComponent activityComponent;
    private int activityId;

    /**
     * Give this list, to let {@link AbstractBaseActivity} know whether inheriting classes
     * using view controllers or not. All controller instances included in this list
     * will be automatically detached on {@link Activity#onDestroy()}.
     */
    protected abstract List<? extends BaseViewController<? extends LifecycleObservableView>> getControllers();

    @Override
    protected void onCreate(final @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logLifecycle(String.format("onCreate:bundle %s", savedInstanceState));
        setCurrentLifecycle(LifeCycle.ON_CREATE);

        this.activityId = savedInstanceState != null ?
                savedInstanceState.getInt(KEY_ACTIVITY_ID) : ID_HOLDER.getAndIncrement();
        PersistOnConfigChangeComponent configPersistentComponent;
        if (COMPONENTS_MAP.get(activityId) == null) {
            configPersistentComponent = DaggerPersistOnConfigChangeComponent.builder()
                    .appComponent(MyApplication.getInstance().getAppComponent())
                    .build();
            COMPONENTS_MAP.put(activityId, configPersistentComponent);
            logLifecycle(String.format("Creating new ConfigPersistentComponent(%s) of %s",
                    configPersistentComponent.toString(), activityId));
        } else {
            configPersistentComponent = COMPONENTS_MAP.get(activityId);
            logLifecycle(String.format("Reusing ConfigPersistentComponent(%s) of %s",
                    configPersistentComponent.toString(), activityId));
        }
        this.activityComponent = configPersistentComponent.activityComponent(new ActivityModule(this));
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
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        logLifecycle(String.format("onRestoreInstanceState:bundle %s", savedInstanceState));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_ACTIVITY_ID, activityId);
        logLifecycle(String.format("onSaveInstanceState:bundle %s", outState));
    }

    @Override
    protected void onResume() {
        super.onResume();
        logLifecycle("onResume");
        setCurrentLifecycle(LifeCycle.ON_RESUME);
    }

    @Override
    protected void onPause() {
        logLifecycle("onPause");
        setCurrentLifecycle(LifeCycle.ON_PAUSE);
        super.onPause();
    }

    @Override
    protected void onStop() {
        logLifecycle("onStop");
        setCurrentLifecycle(LifeCycle.ON_STOP);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        logLifecycle("onDestroy");
        setCurrentLifecycle(LifeCycle.ON_DESTROY);

        if (!isChangingConfigurations()) {
            logLifecycle(String.format("Removing ConfigPersistentComponent of %s", activityId));
            COMPONENTS_MAP.remove(activityId);
        }

        if (getControllers() != null) {
            for (final BaseViewController ctrler : getControllers()) {
                if (ctrler != null && ctrler.isAttached()) {
                    ctrler.detachView(isChangingConfigurations());
                }
            }
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(final int reqCode, final int resultCode, final Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        logLifecycle(String.format("onActivityResult: requestCode %s, resultCode %s", reqCode,
                                   resultCode));
        logLifecycle(String.format("onActivityResult: data %s", data));
    }

    protected ActivityComponent getActivityComponent() {
        return activityComponent;
    }

    /**
     * This method will be convenient when managing lifecycle of
     * <a href="http://reactivex.io/documentation/observable.html">Observable</a> subscription using
     * <a href="http://reactivex.io/documentation/operators/takeuntil.html">takeUntil</a> operator.
     * <getControllers>
     * Using <code>takeUntil(Lifecycle)</code> idiom will help us to forget to calling
     * <code>Observable#unsubscribe</code> to avoid memory leak on activity when subscribed
     * <code>Observable</code> lives longer than activity.
     * </getControllers>
     *
     * @return An observable of activity lifecycle state.
     */
    public Observable<Integer> getObservableLifecycle(final @LifeCycle int lifecycle) {
        return getObservableLifecycleInternal().filter(new Func1<Integer, Boolean>() {
            @Override
            public Boolean call(final Integer l) {
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
                    break;
            }
        }
        return lifecycleSubject;
    }

    private void setCurrentLifecycle(final @LifeCycle int lifecycle) {
        currentLifecycle = lifecycle;
        if (null != lifecycleSubject) {
            lifecycleSubject.onNext(lifecycle);
            if (lifecycle == LifeCycle.ON_DESTROY) {
                lifecycleSubject.onCompleted();
                lifecycleSubject = null;
            }
        }
    }

    private void logLifecycle(final String lifecycle) {
        if (BuildConfig.DEBUG) {
            final Logger log = LogFactory.getLogger(this.getClass().getSimpleName());
            log.v("entered lifecycle: %s", lifecycle);
        }
    }

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
        int BEFORE_ON_CREATE = Integer.MIN_VALUE;
        int ON_CREATE        = 1;
        int ON_START         = 2;
        int ON_RESUME        = 3;
        int ON_PAUSE         = 4;
        int ON_STOP          = 5;
        int ON_DESTROY       = 6;
    }
}
