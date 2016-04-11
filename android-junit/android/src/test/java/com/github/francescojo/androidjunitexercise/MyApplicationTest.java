/*
 * @(#)MyApplicationTest.java $Version 13 - Nov - 2015
 *
 * Distributed under no licences and no warranty.
 */
package com.github.francescojo.androidjunitexercise;

import com.github.francescojo.androidjunitexercise._testbase.AndroidTestBase;

import org.junit.Test;
import org.robolectric.RuntimeEnvironment;

import static org.junit.Assert.assertTrue;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 */
public class MyApplicationTest extends AndroidTestBase {
    @Test
    public void testGetInstance() throws Exception {
        assertTrue(RuntimeEnvironment.application instanceof MyApplication);
    }
}
