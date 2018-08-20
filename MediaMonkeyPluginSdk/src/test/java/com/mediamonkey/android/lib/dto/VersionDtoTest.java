/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.lib.dto;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Nov - 2016
 */
public class VersionDtoTest {
    @Test
    public void testEqual() throws Exception {
        VersionDto v1 = new VersionDto("test", 1, 0, 0);
        VersionDto v2 = new VersionDto("test", 1, 0, 0);

        int expected = 0;
        int actual = v1.compareTo(v2);

        assertEquals(actual, expected);
    }

    @Test
    public void testNewer() throws Exception {
        VersionDto v1 = new VersionDto("test", 1, 1, 1000);
        VersionDto v2 = new VersionDto("test", 1, 0, 5000);

        int expected = 1;
        int actual = v1.compareTo(v2);

        assertEquals(actual, expected);
    }


    @Test
    public void testOlder() throws Exception {
        VersionDto v1 = new VersionDto("test", 1, 9, 0);
        VersionDto v2 = new VersionDto("test", 2, 0, 0);

        int expected = -1;
        int actual = v1.compareTo(v2);

        assertEquals(actual, expected);
    }
}
