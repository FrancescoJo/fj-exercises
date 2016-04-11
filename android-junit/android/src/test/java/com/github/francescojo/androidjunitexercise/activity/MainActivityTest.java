/*
 * @(#)MainActivityTest.java $Version 13 - Nov - 2015
 *
 * Distributed under no licences and no warranty.
 */
package com.github.francescojo.androidjunitexercise.activity;

import android.widget.TextView;

import com.github.francescojo.androidjunitexercise.MyApplication;
import com.github.francescojo.androidjunitexercise.R;
import com.github.francescojo.androidjunitexercise._testbase.AndroidActivityTestBase;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 */
public class MainActivityTest extends AndroidActivityTestBase<MainActivity> {
    private MainActivity activity;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        this.activity = onCreate();
    }

    @Test
    public void testTextViewCaption() throws Exception {
        TextView tv = (TextView) activity.findViewById(R.id.textView);

        String expected = MyApplication.getInstance().getString(R.string.hello_world);
        String actual = tv.getText().toString();

        assertEquals(expected, actual);
    }

    @Override
    protected Class<MainActivity> getActivityClass() {
        return MainActivity.class;
    }
}
