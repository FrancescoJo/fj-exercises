/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.util.io;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 24 - Nov - 2016
 */
public final class HexUtils {
    private static final char[] HEX_CODE_UPPER = new char[] {
        '0', '1', '2', '3', '4', '5', '6', '7',
        '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };
    private static final char[] HEX_CODE_LOWER = new char[] {
        '0', '1', '2', '3', '4', '5', '6', '7',
        '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    public static String hex2StrUpper(byte[] data) {
        return hex2StrInternal(data, HEX_CODE_UPPER);
    }

    public static String hex2StrLower(byte[] data) {
        return hex2StrInternal(data, HEX_CODE_LOWER);
    }

    private static String hex2StrInternal(byte[] data, char[] dictionary) {
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(dictionary[(b >> 4) & 0xF]);
            r.append(dictionary[(b & 0xF)]);
        }
        return r.toString();
    }

    private HexUtils() { }
}
