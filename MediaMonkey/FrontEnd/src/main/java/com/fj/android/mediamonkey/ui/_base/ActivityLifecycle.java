/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.ui._base;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 23 - Dec - 2016
 */
public enum ActivityLifecycle {
    PRE_ON_CREATE(0),
    ON_CREATE(1),
    ON_START(2),
    ON_RESUME(3),
    ON_PAUSE(4),
    ON_STOP(5),
    ON_DESTROY(6);

    final int state;

    ActivityLifecycle(int state) {
        this.state = state;
    }
}
