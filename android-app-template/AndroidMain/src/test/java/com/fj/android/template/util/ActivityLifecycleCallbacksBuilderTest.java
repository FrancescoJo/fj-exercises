/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.fj.android.template.util;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import com.fj.android.template._base.AndroidTestBase;
import com.fj.android.template._util.ValueReference;
import com.fj.android.template._mock.AbstractBaseActivityImpl;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;
import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;
import org.robolectric.util.ActivityController;

import static org.junit.Assert.assertTrue;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Aug - 2016
 */
public class ActivityLifecycleCallbacksBuilderTest extends AndroidTestBase {
    private ActivityLifecycleCallbacksBuilder            builder;
    private ActivityController<AbstractBaseActivityImpl> activityController;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        this.builder = new ActivityLifecycleCallbacksBuilder();
        this.activityController = Robolectric.buildActivity(AbstractBaseActivityImpl.class);
    }

    @Test
    public void testOnCreateCalled() throws Exception {
        final ValueReference<Boolean> assertRef = new ValueReference<>(false);
        Application.ActivityLifecycleCallbacks callback = builder.onActivityCreated(new BiConsumer<Activity, Bundle>() {
            @Override
            public void accept(Activity activity, Bundle bundle) throws Exception {
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
        Application.ActivityLifecycleCallbacks callback = builder.onActivityStarted(new Consumer<Activity>() {
            @Override
            public void accept(Activity activity) throws Exception {
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
        Application.ActivityLifecycleCallbacks callback = builder.onActivityResumed(new Consumer<Activity>() {
            @Override
            public void accept(Activity activity) throws Exception {
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
        Application.ActivityLifecycleCallbacks callback = builder.onActivityPaused(new Consumer<Activity>() {
            @Override
            public void accept(Activity activity) throws Exception {
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
        Application.ActivityLifecycleCallbacks callback = builder.onActivityStopped(new Consumer<Activity>() {
            @Override
            public void accept(Activity activity) throws Exception {
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
        Application.ActivityLifecycleCallbacks callback = builder.onActivitySaveInstanceState(new BiConsumer<Activity, Bundle>() {
            @Override
            public void accept(Activity activity, Bundle bundle) throws Exception {
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
        Application.ActivityLifecycleCallbacks callback = builder.onActivityDestroyed(new Consumer<Activity>() {
            @Override
            public void accept(Activity activity) throws Exception {
                assertRef.set(true);
            }
        }).build();

        getApplication().registerActivityLifecycleCallbacks(callback);
        activityController.create().start().resume().pause().stop().saveInstanceState(new Bundle()).destroy();
        assertTrue("onActivityResumed callback must be invoked and value changed", assertRef.get());
        getApplication().unregisterActivityLifecycleCallbacks(callback);
    }
}
