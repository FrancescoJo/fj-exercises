/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.example.hwan.myapplication.util;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.example.hwan.myapplication.MyActivity;
import com.example.hwan.myapplication._base.AndroidTestBase;
import com.example.hwan.myapplication._base.ValueReference;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;
import org.robolectric.util.ActivityController;

import rx.functions.Action1;
import rx.functions.Action2;

import static org.junit.Assert.assertTrue;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Aug - 2016
 */
public class ActivityLifecycleCallbacksBuilderTest extends AndroidTestBase {
    private ActivityLifecycleCallbacksBuilder builder;
    private ActivityController<MyActivity> activityController;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        this.builder = new ActivityLifecycleCallbacksBuilder();
        this.activityController = Robolectric.buildActivity(MyActivity.class);
    }

    @Test
    public void testOnCreateCalled() throws Exception {
        final ValueReference<Boolean> assertRef = new ValueReference<>(false);
        Application.ActivityLifecycleCallbacks callback =
                builder.onActivityCreated(new Action2<Activity, Bundle>() {
                    @Override
                    public void call(Activity activity, Bundle bundle) {
                        assertRef.set(true);
                    }
                }).build();

        getApplication().registerActivityLifecycleCallbacks(callback);
        activityController.create();
        assertTrue("onActivityCreated callback must be invoked and value changed", assertRef.get());
        getApplication().unregisterActivityLifecycleCallbacks(callback);
    }

    @Test
    public void testOnStartCalled() throws Exception {
        final ValueReference<Boolean> assertRef = new ValueReference<>(false);
        Application.ActivityLifecycleCallbacks callback =
                builder.onActivityStarted(new Action1<Activity>() {
                    @Override
                    public void call(Activity activity) {
                        assertRef.set(true);
                    }
                }).build();

        getApplication().registerActivityLifecycleCallbacks(callback);
        activityController.create().start();
        assertTrue("onActivityStarted callback must be invoked and value changed", assertRef.get());
        getApplication().unregisterActivityLifecycleCallbacks(callback);
    }

    @Test
    public void testOnResumeCalled() throws Exception {
        final ValueReference<Boolean> assertRef = new ValueReference<>(false);
        Application.ActivityLifecycleCallbacks callback =
                builder.onActivityResumed(new Action1<Activity>() {
                    @Override
                    public void call(Activity activity) {
                        assertRef.set(true);
                    }
                }).build();

        getApplication().registerActivityLifecycleCallbacks(callback);
        activityController.create().start().resume();
        assertTrue("onActivityResumed callback must be invoked and value changed", assertRef.get());
        getApplication().unregisterActivityLifecycleCallbacks(callback);
    }

    @Test
    public void testOnPauseCalled() throws Exception {
        final ValueReference<Boolean> assertRef = new ValueReference<>(false);
        Application.ActivityLifecycleCallbacks callback =
                builder.onActivityPaused(new Action1<Activity>() {
                    @Override
                    public void call(Activity activity) {
                        assertRef.set(true);
                    }
                }).build();

        getApplication().registerActivityLifecycleCallbacks(callback);
        activityController.create().start().resume().pause();
        assertTrue("onActivityResumed callback must be invoked and value changed", assertRef.get());
        getApplication().unregisterActivityLifecycleCallbacks(callback);
    }

    @Test
    public void testOnStopCalled() throws Exception {
        final ValueReference<Boolean> assertRef = new ValueReference<>(false);
        Application.ActivityLifecycleCallbacks callback =
                builder.onActivityStopped(new Action1<Activity>() {
                    @Override
                    public void call(Activity activity) {
                        assertRef.set(true);
                    }
                }).build();

        getApplication().registerActivityLifecycleCallbacks(callback);
        activityController.create().start().resume().pause().stop();
        assertTrue("onActivityResumed callback must be invoked and value changed", assertRef.get());
        getApplication().unregisterActivityLifecycleCallbacks(callback);
    }

    @Test
    public void testOnSaveInstanceStateCalled() throws Exception {
        final ValueReference<Boolean> assertRef = new ValueReference<>(false);
        Application.ActivityLifecycleCallbacks callback =
                builder.onActivitySaveInstanceState(new Action2<Activity, Bundle>() {
                    @Override
                    public void call(Activity activity, Bundle bundle) {
                        assertRef.set(true);
                    }
                }).build();
        getApplication().registerActivityLifecycleCallbacks(callback);
        activityController.create().start().resume().pause().stop().saveInstanceState(new Bundle());
        assertTrue("onActivityResumed callback must be invoked and value changed", assertRef.get());
        getApplication().unregisterActivityLifecycleCallbacks(callback);
    }

    @Test
    public void testOnDestroyCalled() throws Exception {
        final ValueReference<Boolean> assertRef = new ValueReference<>(false);
        Application.ActivityLifecycleCallbacks callback =
                builder.onActivityDestroyed(new Action1<Activity>() {
                    @Override
                    public void call(Activity activity) {
                        assertRef.set(true);
                    }
                }).build();

        getApplication().registerActivityLifecycleCallbacks(callback);
        activityController.create().start().resume().pause().stop().saveInstanceState(new Bundle()).destroy();
        assertTrue("onActivityResumed callback must be invoked and value changed", assertRef.get());
        getApplication().unregisterActivityLifecycleCallbacks(callback);
    }
}
