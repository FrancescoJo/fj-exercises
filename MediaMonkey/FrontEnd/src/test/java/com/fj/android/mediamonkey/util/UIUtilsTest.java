/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.util;

import com.fj.android.mediamonkey.R;
import com.fj.android.mediamonkey._base.AndroidTestBase;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 16 - Dec - 2016
 */
public class UIUtilsTest extends AndroidTestBase {
    @Test
    public void testGetRelativeDateText_future() throws Exception {
        String expected = "";
        String actual = UiUtils.getRelativeDateText(0, 1);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetRelativeDateText_today() throws Exception {
        String expected = ResourceUtils.getString(R.string.text_date_today);
        String actual = UiUtils.getRelativeDateText(0, 0);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetRelativeDateText_yesterday1() throws Exception {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.set(2015, 12, 3, 0, 0, 0);
        c2.set(2015, 12, 2, 23, 59, 59);

        String expected = ResourceUtils.getString(R.string.text_date_yesterday);
        String actual = UiUtils.getRelativeDateText(c1.getTimeInMillis(), c2.getTimeInMillis());

        assertEquals(expected, actual);
    }


    @Test
    public void testGetRelativeDateText_yesterday2() throws Exception {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.set(2015, 12, 3, 0, 0, 0);
        c2.set(2015, 12, 2, 0, 0, 0);

        String expected = ResourceUtils.getString(R.string.text_date_yesterday);
        String actual = UiUtils.getRelativeDateText(c1.getTimeInMillis(), c2.getTimeInMillis());

        assertEquals(expected, actual);
    }

    @Test
    public void testGetRelativeDateText_nDaysAgo1() throws Exception {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.clear();
        c1.set(2015, 12, 3, 0, 0, 0);
        c2.clear();
        c2.set(2015, 12, 1, 23, 59, 59);

        String expected = ResourceUtils.getPlurals(R.plurals.text_date_days_ago, 2, 2);
        String actual = UiUtils.getRelativeDateText(c1.getTimeInMillis(), c2.getTimeInMillis());

        assertEquals(expected, actual);
    }

    @Test
    public void testGetRelativeDateText_nDaysAgo2() throws Exception {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.clear();
        c1.set(2015, 12, 30, 0, 0, 0);
        c2.clear();
        c2.set(2015, 12, 24, 0, 0, 0);

        String expected = ResourceUtils.getPlurals(R.plurals.text_date_days_ago, 6, 6);
        String actual = UiUtils.getRelativeDateText(c1.getTimeInMillis(), c2.getTimeInMillis());

        assertEquals(expected, actual);
    }
}
