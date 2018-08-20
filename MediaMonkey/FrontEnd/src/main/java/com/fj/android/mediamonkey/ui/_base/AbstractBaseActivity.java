/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.ui._base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;

import com.fj.android.mediamonkey.BuildConfig;
import com.fj.android.mediamonkey.MediaMonkeyApplication;
import com.fj.android.mediamonkey.inject.component.ActivityComponent;
import com.fj.android.mediamonkey.inject.component.DaggerPersistOnConfigChangeComponent;
import com.fj.android.mediamonkey.inject.component.PersistOnConfigChangeComponent;
import com.fj.android.mediamonkey.inject.module.ActivityModule;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Observable;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;
import timber.log.Timber;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 01 - Sep - 2016
 */
public abstract class AbstractBaseActivity extends AppCompatActivity {
    private static final String                                      KEY_ACTIVITY_ID = AbstractBaseActivity.class
            .getCanonicalName() + ".KEY_ACTIVITY_ID";
    private static final AtomicInteger                               ID_HOLDER       = new AtomicInteger(0);
    private static final SparseArray<PersistOnConfigChangeComponent> COMPONENTS_MAP  = new SparseArray<>();

    private ActivityComponent          activityComponent;
    private int                        activityId;
    private Subject<ActivityLifecycle> lifecycleSubject;
    private ActivityLifecycle          currentLifeCycle = ActivityLifecycle.PRE_ON_CREATE;

    /**
     * Give this list, to let {@link AbstractBaseActivity} know whether inheriting classes
     * using view presenters or not. All presenter instances included in this list
     * will be automatically detached on {@link Activity#onDestroy()}.
     */
    protected abstract List<? extends BasePresenter> getPresenters();

    @Override
    protected void onCreate(final @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLifecycle(String.format("onCreate:bundle %s", savedInstanceState), ActivityLifecycle.ON_CREATE);

        this.activityId = savedInstanceState != null ? savedInstanceState.getInt(KEY_ACTIVITY_ID) : ID_HOLDER
                .getAndIncrement();
        PersistOnConfigChangeComponent configPersistentComponent;
        if (COMPONENTS_MAP.get(activityId) == null) {
            configPersistentComponent = DaggerPersistOnConfigChangeComponent.builder()
                    .appComponent(MediaMonkeyApplication.getInstance().getAppComponent())
                    .build();
            COMPONENTS_MAP.put(activityId, configPersistentComponent);
            Timber.v("Creating new ConfigPersistentComponent(%s) of %s", configPersistentComponent
                    .toString(), activityId);
        } else {
            configPersistentComponent = COMPONENTS_MAP.get(activityId);
            Timber.v("Reusing ConfigPersistentComponent(%s) of %s", configPersistentComponent
                    .toString(), activityId);
        }
        this.activityComponent = configPersistentComponent.activityComponent(new ActivityModule(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        setLifecycle("onStart", ActivityLifecycle.ON_START);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        log("onRestart");
    }

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        log(String.format("onRestoreInstanceState:bundle %s", savedInstanceState));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_ACTIVITY_ID, activityId);
        log(String.format("onSaveInstanceState:bundle %s", outState));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLifecycle("onResume", ActivityLifecycle.ON_RESUME);
    }

    @Override
    protected void onPause() {
        setLifecycle("onPause", ActivityLifecycle.ON_PAUSE);
        super.onPause();
    }

    @Override
    protected void onStop() {
        setLifecycle("onStop", ActivityLifecycle.ON_STOP);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        setLifecycle("onDestroy", ActivityLifecycle.ON_DESTROY);

        if (!isChangingConfigurations()) {
            log(String.format("Removing ConfigPersistentComponent of %s", activityId));
            COMPONENTS_MAP.remove(activityId);
        }

        if (getPresenters() != null) {
            for (final BasePresenter ctrler : getPresenters()) {
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
        log(String.format("onActivityResult: requestCode %s, resultCode %s", reqCode, resultCode));
        log(String.format("onActivityResult: data %s", data));
    }

    protected final ActivityComponent getActivityComponent() {
        return activityComponent;
    }

    private Observable<ActivityLifecycle> getLcObservable() {
        if (null == lifecycleSubject) {
            lifecycleSubject = BehaviorSubject.create();
        }

        if (currentLifeCycle == ActivityLifecycle.ON_DESTROY) {
            return Observable.just(ActivityLifecycle.ON_DESTROY);
        } else {
            return lifecycleSubject;
        }
    }

    protected final Observable<ActivityLifecycle> getObservableLifeCycle(final ActivityLifecycle demandingLifecycle) {
        return getLcObservable()
                .filter(new Predicate<ActivityLifecycle>() {
                    @Override
                    public boolean test(ActivityLifecycle lifecycle) throws Exception {
                        return lifecycle.state >= demandingLifecycle.state;
                    }
                }).take(1);
    }

    private void setLifecycle(final String logMsg, ActivityLifecycle lifecycle) {
        if (null != lifecycleSubject) {
            lifecycleSubject.onNext(lifecycle);
        }

        log(logMsg);
    }

    private void log(final String lifecycle) {
        if (BuildConfig.DEBUG) {
            Timber.v("entered lifecycle: %s", lifecycle);
        }
    }
}
