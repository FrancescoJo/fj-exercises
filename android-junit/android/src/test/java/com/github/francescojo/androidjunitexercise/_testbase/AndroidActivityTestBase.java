/*
 * @(#)TestBaseAndroidActivity.java $Version 13 - Nov - 2015
 *
 * Distributed under no licences and no warranty.
 */
package com.github.francescojo.androidjunitexercise._testbase;

import android.app.Activity;
import android.os.Bundle;

import org.junit.Before;
import org.junit.Ignore;
import org.robolectric.Robolectric;
import org.robolectric.util.ActivityController;

/**
 * Common test configuration for Activity lifecycle dependent logic.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 */
@Ignore("Test base class with Android resources & activity lifecycle control")
public abstract class AndroidActivityTestBase<T extends Activity> extends AndroidTestBase {
    private ActivityController<T> activityController;

    @Before
    public void setUp() throws Exception {
        activityController = Robolectric.buildActivity(getActivityClass());
    }

    protected ActivityController<T> onCreate() {
        return activityController.create();
    }

    protected ActivityController<T> onCreate(Bundle savedInstanceState) {
        return activityController.create(savedInstanceState);
    }

    protected abstract Class<T> getActivityClass();
}
