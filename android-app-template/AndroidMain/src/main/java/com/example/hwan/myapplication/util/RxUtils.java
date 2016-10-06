/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.example.hwan.myapplication.util;

import rx.Subscription;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 18 - Oct - 2016
 */
public final class RxUtils {
    public static boolean isSubscribed(Subscription subscription) {
        return subscription != null && !subscription.isUnsubscribed();
    }

    public static void unsubscribe(Subscription subscription) {
        if (isSubscribed(subscription)) {
            subscription.unsubscribe();
        }
    }

    private RxUtils() {}
}
