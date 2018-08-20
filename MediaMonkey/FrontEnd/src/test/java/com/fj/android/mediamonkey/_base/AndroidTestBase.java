/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.fj.android.mediamonkey._base;

import android.support.annotation.CallSuper;

import com.fj.android.mediamonkey.BuildConfig;
import com.fj.android.mediamonkey.MediaMonkeyApplication;
import com.fj.android.mediamonkey.inject.component.AppComponent;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import io.reactivex.Scheduler;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;

import static org.mockito.Mockito.mock;

/**
 * Common test configuration for Android dependent logic.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Aug - 2016
 */
@Ignore("Test base class with Android resources")
@RunWith(MyRobolectricGradleTestRunner.class)
@Config(application = MediaMonkeyApplication.class,
        constants = BuildConfig.class,
        sdk = MyRobolectricGradleTestRunner.SDK_VERSION)
public class AndroidTestBase extends JavaTestBase {
    private MediaMonkeyApplication application;
    @Mock
    private AppComponent mockAppComponent;

    @CallSuper
    @Before
    public void setUp() throws Exception {
        this.application = (MediaMonkeyApplication) RuntimeEnvironment.application;
        MockitoAnnotations.initMocks(this);
        this.mockAppComponent = mock(AppComponent.class);
        application.setAppComponent(mockAppComponent);

        RxJavaPlugins.setIoSchedulerHandler(new Function<Scheduler, Scheduler>() {
            @Override
            public Scheduler apply(Scheduler scheduler) throws Exception {
                return new ImmediateScheduler();
            }
        });
    }

    protected MediaMonkeyApplication getApplication() {
        return application;
    }

    @CallSuper
    @After
    public void tearDown() throws Exception {
        this.mockAppComponent = null;
        application.setAppComponent(null);
    }
}