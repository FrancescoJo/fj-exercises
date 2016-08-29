/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.example.hwan.myapplication.util;

import org.junit.Test;

import java.lang.reflect.Array;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Sep - 2016
 */
public class ArrayUtilsTest {
    // region testIsEmptyXXX
    /*
     * TestIsEmptyXXX cases are cannot be generalised by generic
     * Because of type erasure on runtime
     */
    @Test
    public void testIsEmptyBooleanArray() {
        assertTrue (ArrayUtils.isEmpty((boolean[]) null));
        assertTrue (ArrayUtils.isEmpty(new boolean[] {}));
        assertFalse(ArrayUtils.isEmpty((boolean[]) Array.newInstance(boolean.class, 1)));
    }

    @Test
    public void testIsEmptyByteArray() {
        assertTrue (ArrayUtils.isEmpty((byte[]) null));
        assertTrue (ArrayUtils.isEmpty(new byte[] {}));
        assertFalse(ArrayUtils.isEmpty((byte[]) Array.newInstance(byte.class, 1)));
    }

    @Test
    public void testIsEmptyCharArray() {
        assertTrue (ArrayUtils.isEmpty((char[]) null));
        assertTrue (ArrayUtils.isEmpty(new char[] {}));
        assertFalse(ArrayUtils.isEmpty((char[]) Array.newInstance(char.class, 1)));
    }

    @Test
    public void testIsEmptyDoubleArray() {
        assertTrue (ArrayUtils.isEmpty((double[]) null));
        assertTrue (ArrayUtils.isEmpty(new double[] {}));
        assertFalse(ArrayUtils.isEmpty((double[]) Array.newInstance(double.class, 1)));
    }

    @Test
    public void testIsEmptyFloatArray() {
        assertTrue (ArrayUtils.isEmpty((float[]) null));
        assertTrue (ArrayUtils.isEmpty(new float[] {}));
        assertFalse(ArrayUtils.isEmpty((float[]) Array.newInstance(float.class, 1)));
    }

    @Test
    public void testIsEmptyIntArray() {
        assertTrue (ArrayUtils.isEmpty((int[]) null));
        assertTrue (ArrayUtils.isEmpty(new int[] {}));
        assertFalse(ArrayUtils.isEmpty((int[]) Array.newInstance(int.class, 1)));
    }

    @Test
    public void testIsEmptyLongArray() {
        assertTrue (ArrayUtils.isEmpty((long[]) null));
        assertTrue (ArrayUtils.isEmpty(new long[] {}));
        assertFalse(ArrayUtils.isEmpty((long[]) Array.newInstance(long.class, 1)));
    }

    @Test
    public void testIsEmptyShortArray() {
        assertTrue (ArrayUtils.isEmpty((short[]) null));
        assertTrue (ArrayUtils.isEmpty(new short[] {}));
        assertFalse(ArrayUtils.isEmpty((short[]) Array.newInstance(short.class, 1)));
    }

    @Test
    public void testIsEmptyObjectArray() {
        assertTrue (ArrayUtils.isEmpty((Object[]) null));
        assertTrue (ArrayUtils.isEmpty(new Object[] {}));
        assertFalse(ArrayUtils.isEmpty((Object[]) Array.newInstance(Object.class, 1)));
    }
    // endregion testIsEmptyXXX
}
