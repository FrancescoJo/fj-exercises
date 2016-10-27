/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.fj.android.template.ui.main;

import com.fj.android.template._base.AndroidTestBase;
import com.fj.android.template.dto.CounterDto;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;
import org.robolectric.util.ActivityController;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 18 - Nov - 2016
 */
public class MainActivityTest extends AndroidTestBase {
    private ActivityController<MainActivity> activityController;
    private MainActivity                               activity;
    private MainActivityPresenter                 mockPresenter;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        this.activityController = Robolectric.buildActivity(MainActivity.class);
        this.activity = activityController.get();
        this.mockPresenter = mock(MainActivityPresenter.class);
        activityController.create();
        activity.presenter = mockPresenter;
    }

    @Test
    public void testShowCounterResult() throws Exception {
        activityController.start().resume();
        int expectedCount = 15;
        CounterDto counter = mock(CounterDto.class);
        when(counter.getCounter()).thenReturn(expectedCount);
        String expected = "Number of visitors: " + expectedCount;

        activity.showCounterResult(counter);

        String actual = activity.textView2.getText().toString();

        assertEquals(expected, actual);
    }
}
