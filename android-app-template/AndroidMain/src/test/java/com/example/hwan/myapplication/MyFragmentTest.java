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
import org.robolectric.util.FragmentController;

import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.junit.Assert.assertEquals;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Sep - 2016
 */
public class MyFragmentTest extends AndroidTestBase {
    private FragmentController<MyFragment> fragmentController;
    private MyFragment fragment;

    @Before
    public void setUp() throws Exception {
        this.fragmentController = Robolectric.buildFragment(MyFragment.class);
        this.fragment = fragmentController.get();
    }

    @Test
    public void testAllEmits() throws Exception {
        Observable<Integer> lifecycle = fragment.getObservableLifecycle(MyFragment.LifeCycle.ON_STOP);
        TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
        lifecycle.subscribe(testSubscriber);

        fragmentController.attach();
        assertEquals("Emission must not be completed on onAttach", 0, testSubscriber.getCompletions());

        fragmentController.create();
        fragmentController.start();
        fragmentController.resume();
        fragmentController.pause();
        assertEquals("Emission must not be completed before onStop", 0, testSubscriber.getCompletions());

        fragmentController.stop();
        assertEquals("Emission must be completed on onStop", 1, testSubscriber.getCompletions());

        fragmentController.destroy();
        assertEquals("No emissions after onStop", 1, testSubscriber.getValueCount());

        @SuppressWarnings("unchecked")
        List<Integer> expected = Collections.singletonList(MyFragment.LifeCycle.ON_STOP);
        List<Integer> actual = testSubscriber.getOnNextEvents();
        assertListEquals("", expected, actual);
    }

    @Test
    public void testNoEmitsBeforeCreate() throws Exception {
        Observable<Integer> lifecycle = fragment.getObservableLifecycle(MyFragment.LifeCycle.ON_CREATE);
        TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
        lifecycle.subscribe(testSubscriber);

        // Nothing should emitted before activity is created
        testSubscriber.assertNoValues();
    }
}
