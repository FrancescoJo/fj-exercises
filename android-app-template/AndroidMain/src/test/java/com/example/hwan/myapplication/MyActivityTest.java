/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.example.hwan.myapplication;

import com.example.hwan.myapplication._base.AndroidTestBase;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;
import org.robolectric.util.ActivityController;

import java.util.List;

import edu.emory.mathcs.backport.java.util.Collections;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.junit.Assert.assertEquals;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Sep - 2016
 */
public class MyActivityTest extends AndroidTestBase {
    private ActivityController<MyActivity> activityController;
    private MyActivity activity;

    @Before
    public void setUp() throws Exception {
        this.activityController = Robolectric.buildActivity(MyActivity.class);
        this.activity = activityController.get();
    }

    @Test
    public void testAllEmits() throws Exception {
        Observable<Integer> lifecycle = activity.getObservableLifecycle(MyActivity.LifeCycle.ON_RESUME);
        TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
        lifecycle.subscribe(testSubscriber);

        activityController.create();
        assertEquals("Emission must not be completed on onCreate", 0, testSubscriber.getCompletions());

        activityController.start();
        assertEquals("Emission must not be completed on onStart", 0, testSubscriber.getCompletions());

        activityController.resume();
        assertEquals("Emission must be completed on onResume", 1, testSubscriber.getCompletions());

        activityController.pause();
        assertEquals("No emissions after onResume", 1, testSubscriber.getCompletions());

        activityController.stop();
        activityController.destroy();
        assertEquals("No emissions after onStop", 1, testSubscriber.getValueCount());

        @SuppressWarnings("unchecked")
        List<Integer> expected = Collections.singletonList(MyActivity.LifeCycle.ON_RESUME);
        List<Integer> actual = testSubscriber.getOnNextEvents();
        assertListEquals("", expected, actual);
    }

    @Test
    public void testNoEmitsBeforeCreate() throws Exception {
        Observable<Integer> lifecycle = activity.getObservableLifecycle(MyActivity.LifeCycle.ON_CREATE);
        TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
        lifecycle.subscribe(testSubscriber);

        // Nothing should emitted before activity is created
        testSubscriber.assertNoValues();
    }
}
