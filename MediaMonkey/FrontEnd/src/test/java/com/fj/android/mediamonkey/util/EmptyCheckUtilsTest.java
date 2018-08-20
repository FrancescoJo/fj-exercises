/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.fj.android.mediamonkey.util;

import com.fj.android.mediamonkey.util.lang.EmptyCheckUtils;

import org.junit.Test;

import java.lang.reflect.Array;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Sep - 2016
 */
public class EmptyCheckUtilsTest {
    // region testIsEmptyXXX
    /*
     * TestIsEmptyXXX cases are cannot be generalised by generic
     * Because of type erasure on runtime
     */
    @Test
    public void testIsEmptyBooleanArray() {
        assertTrue(EmptyCheckUtils.isEmpty((boolean[]) null));
        assertTrue(EmptyCheckUtils.isEmpty(new boolean[] { }));
        assertFalse(EmptyCheckUtils.isEmpty((boolean[]) Array.newInstance(boolean.class, 1)));
    }

    @Test
    public void testIsEmptyByteArray() {
        assertTrue(EmptyCheckUtils.isEmpty((byte[]) null));
        assertTrue(EmptyCheckUtils.isEmpty(new byte[] { }));
        assertFalse(EmptyCheckUtils.isEmpty((byte[]) Array.newInstance(byte.class, 1)));
    }

    @Test
    public void testIsEmptyCharArray() {
        assertTrue(EmptyCheckUtils.isEmpty((char[]) null));
        assertTrue(EmptyCheckUtils.isEmpty(new char[] { }));
        assertFalse(EmptyCheckUtils.isEmpty((char[]) Array.newInstance(char.class, 1)));
    }

    @Test
    public void testIsEmptyDoubleArray() {
        assertTrue(EmptyCheckUtils.isEmpty((double[]) null));
        assertTrue(EmptyCheckUtils.isEmpty(new double[] { }));
        assertFalse(EmptyCheckUtils.isEmpty((double[]) Array.newInstance(double.class, 1)));
    }

    @Test
    public void testIsEmptyFloatArray() {
        assertTrue(EmptyCheckUtils.isEmpty((float[]) null));
        assertTrue(EmptyCheckUtils.isEmpty(new float[] { }));
        assertFalse(EmptyCheckUtils.isEmpty((float[]) Array.newInstance(float.class, 1)));
    }

    @Test
    public void testIsEmptyIntArray() {
        assertTrue(EmptyCheckUtils.isEmpty((int[]) null));
        assertTrue(EmptyCheckUtils.isEmpty(new int[] { }));
        assertFalse(EmptyCheckUtils.isEmpty((int[]) Array.newInstance(int.class, 1)));
    }

    @Test
    public void testIsEmptyLongArray() {
        assertTrue(EmptyCheckUtils.isEmpty((long[]) null));
        assertTrue(EmptyCheckUtils.isEmpty(new long[] { }));
        assertFalse(EmptyCheckUtils.isEmpty((long[]) Array.newInstance(long.class, 1)));
    }

    @Test
    public void testIsEmptyShortArray() {
        assertTrue(EmptyCheckUtils.isEmpty((short[]) null));
        assertTrue(EmptyCheckUtils.isEmpty(new short[] { }));
        assertFalse(EmptyCheckUtils.isEmpty((short[]) Array.newInstance(short.class, 1)));
    }

    @Test
    public void testIsEmptyObjectArray() {
        assertTrue(EmptyCheckUtils.isEmpty((Object[]) null));
        assertTrue(EmptyCheckUtils.isEmpty(new Object[] { }));
        assertFalse(EmptyCheckUtils.isEmpty((Object[]) Array.newInstance(Object.class, 1)));
    }
    // endregion testIsEmptyXXX
}
