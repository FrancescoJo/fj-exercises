/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.example.hwan.myapplication.ui.base;

import rx.Observable;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 18 - Oct - 2016
 */
public interface LifecycleObservableView {
    Observable<Integer> getObservableLifecycle(final int lifecycle);
}
