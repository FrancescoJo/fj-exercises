/*
 * @(#)AndroidTestBase.java $Version 13 - Nov - 2015
 *
 * Licenced under Apache License v2.0.
 * Read http://www.apache.org/licenses/ for details.
 */
package com.github.francescojo.androidjunitexercise._testbase;

import com.github.francescojo.androidjunitexercise.BuildConfig;
import com.github.francescojo.androidjunitexercise.MyApplication;
import com.github.francescojo.androidjunitexercise.MyRobolectricGradleTestRunner;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

/**
 * Common test configuration for Android dependent logic.
 *
 * @author Hwan Jo(nimbusob@gmail.com)
 */
@Ignore("Test base class with Android resources")
@RunWith(MyRobolectricGradleTestRunner.class)
@Config(application = MyApplication.class,
        constants = BuildConfig.class,
        sdk = MyRobolectricGradleTestRunner.SDK_VERSION)
public class AndroidTestBase extends JavaTestBase {
    private MyApplication application;

    @Before
    public void setUp() throws Exception {
        this.application = (MyApplication) RuntimeEnvironment.application;
    }

    protected MyApplication getApplication() {
        return application;
    }
}
