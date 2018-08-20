/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.util;

import io.reactivex.disposables.Disposable;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 18 - Oct - 2016
 */
public final class RxUtils {
    public static boolean isDisposed(Disposable disposable) {
        return disposable != null && !disposable.isDisposed();
    }

    public static void dispose(Disposable disposable) {
        if (isDisposed(disposable)) {
            disposable.dispose();
        }
    }

    private RxUtils() {}
}
