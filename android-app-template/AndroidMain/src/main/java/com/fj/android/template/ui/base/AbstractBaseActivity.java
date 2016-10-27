/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.fj.android.template.ui.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.fj.android.template.BuildConfig;
import com.fj.android.template.MyApplication;
import com.fj.android.template.inject.component.ActivityComponent;
import com.fj.android.template.inject.component.DaggerPersistOnConfigChangeComponent;
import com.fj.android.template.inject.component.PersistOnConfigChangeComponent;
import com.fj.android.template.inject.module.ActivityModule;
import com.fj.android.template.util.logging.LogFactory;
import com.fj.android.template.util.logging.Logger;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 01 - Sep - 2016
 */
public abstract class AbstractBaseActivity extends Activity {
    private static final String                                      KEY_ACTIVITY_ID = AbstractBaseActivity.class
            .getCanonicalName() + ".KEY_ACTIVITY_ID";
    private static final AtomicInteger                               ID_HOLDER       = new AtomicInteger(0);
    private static final SparseArray<PersistOnConfigChangeComponent> COMPONENTS_MAP  = new SparseArray<>();

    private ActivityComponent        activityComponent;
    private int                      activityId;

    /**
     * Give this list, to let {@link AbstractBaseActivity} know whether inheriting classes
     * using view presenters or not. All presenter instances included in this list
     * will be automatically detached on {@link Activity#onDestroy()}.
     */
    protected abstract List<? extends BasePresenter> getPresenters();

    @Override
    protected void onCreate(final @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logLifecycle(String.format("onCreate:bundle %s", savedInstanceState));

        this.activityId = savedInstanceState != null ? savedInstanceState.getInt(KEY_ACTIVITY_ID) : ID_HOLDER
                .getAndIncrement();
        PersistOnConfigChangeComponent configPersistentComponent;
        if (COMPONENTS_MAP.get(activityId) == null) {
            configPersistentComponent = DaggerPersistOnConfigChangeComponent.builder()
                    .appComponent(MyApplication.getInstance().getAppComponent())
                    .build();
            COMPONENTS_MAP.put(activityId, configPersistentComponent);
            logLifecycle(String.format("Creating new ConfigPersistentComponent(%s) of %s", configPersistentComponent
                    .toString(), activityId));
        } else {
            configPersistentComponent = COMPONENTS_MAP.get(activityId);
            logLifecycle(String.format("Reusing ConfigPersistentComponent(%s) of %s", configPersistentComponent
                    .toString(), activityId));
        }
        this.activityComponent = configPersistentComponent.activityComponent(new ActivityModule(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        logLifecycle("onStart");
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
    }

    @Override
    protected void onPause() {
        logLifecycle("onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        logLifecycle("onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        logLifecycle("onDestroy");

        if (!isChangingConfigurations()) {
            logLifecycle(String.format("Removing ConfigPersistentComponent of %s", activityId));
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
        logLifecycle(String.format("onActivityResult: requestCode %s, resultCode %s", reqCode, resultCode));
        logLifecycle(String.format("onActivityResult: data %s", data));
    }

    protected ActivityComponent getActivityComponent() {
        return activityComponent;
    }

    private void logLifecycle(final String lifecycle) {
        if (BuildConfig.DEBUG) {
            final Logger log = LogFactory.getLogger(this.getClass().getSimpleName());
            log.v("entered lifecycle: %s", lifecycle);
        }
    }
}
