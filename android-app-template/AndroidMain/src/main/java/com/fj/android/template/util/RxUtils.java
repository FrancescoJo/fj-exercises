/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.fj.android.template.util;

import io.reactivex.disposables.Disposable;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 18 - Oct - 2016
 */
public final class RxUtils {
    public static boolean isSubscribed(Disposable disposable) {
        return disposable != null && !disposable.isDisposed();
    }

    public static void unsubscribe(Disposable disposable) {
        if (isSubscribed(disposable)) {
            disposable.dispose();
        }
    }

    private RxUtils() {}
}
