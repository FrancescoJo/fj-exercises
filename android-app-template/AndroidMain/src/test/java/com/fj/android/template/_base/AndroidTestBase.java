/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.fj.android.template._base;

import android.support.annotation.CallSuper;

import com.fj.android.template.BuildConfig;
import com.fj.android.template.MyApplication;
import com.fj.android.template.api.ApiService;
import com.fj.android.template.inject.component.AppComponent;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Common test configuration for Android dependent logic.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Aug - 2016
 */
@Ignore("Test base class with Android resources")
@RunWith(MyRobolectricGradleTestRunner.class)
@Config(application = MyApplication.class,
        constants = BuildConfig.class,
        sdk = MyRobolectricGradleTestRunner.SDK_VERSION)
public class AndroidTestBase extends JavaTestBase {
    private MyApplication application;
    @Mock
    private AppComponent mockAppComponent;

    @CallSuper
    @Before
    public void setUp() throws Exception {
        this.application = (MyApplication) RuntimeEnvironment.application;
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

    protected MyApplication getApplication() {
        return application;
    }

    protected void setMockApiService(ApiService mockApiService) {
        when(mockAppComponent.apiService()).thenReturn(mockApiService);
    }

    @CallSuper
    @After
    public void tearDown() throws Exception {
        this.mockAppComponent = null;
        application.setAppComponent(null);
    }
}