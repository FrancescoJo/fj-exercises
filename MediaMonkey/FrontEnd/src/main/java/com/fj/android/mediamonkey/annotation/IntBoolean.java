/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.annotation;

import android.support.annotation.IntDef;

/**
 * Represents boolean as integer as manner of C language for de/serialisation.
 * Useful when converting booleans to int and vice versa, to transfer it
 * via {@link android.os.Bundle}s.
 * <p>
 * This class is useless on API Level 21+ configuration.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 12 - Oct - 2016
 */
@IntDef({ IntBoolean.FALSE, IntBoolean.TRUE })
public @interface IntBoolean {
    int TRUE  = 1;
    int FALSE = 0;
}
