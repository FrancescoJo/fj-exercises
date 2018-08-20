/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.lib.internal;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 10 - Dec - 2016
 */
public final class DataConverter {
    public static List<Long> numStrsToLongList(String[] numStrs) {
        List<Long> list = new ArrayList<>(numStrs.length);
        for (String numStr : numStrs) {
            list.add(Long.parseLong(numStr));
        }

        return list;
    }

    private DataConverter() { }
}
