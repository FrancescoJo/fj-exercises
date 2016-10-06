/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.example.hwan.myapplication._base;

import android.support.annotation.CallSuper;

import com.example.hwan.myapplication.BuildConfig;
import com.example.hwan.myapplication.MyApplication;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

/**
 * Common test configuration for Android dependent logic.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Aug - 2016
 */
// Can we change this to rule based?
@Ignore("Test base class with Android resources")
@RunWith(MyRobolectricGradleTestRunner.class)
@Config(application = MyApplication.class,
        constants = BuildConfig.class,
        sdk = MyRobolectricGradleTestRunner.SDK_VERSION)
public class AndroidTestBase extends JavaTestBase {
    private MyApplication application;

    @CallSuper
    @Before
    public void setUp() throws Exception {
        this.application = (MyApplication) RuntimeEnvironment.application;
    }

    protected MyApplication getApplication() {
        return application;
    }
}