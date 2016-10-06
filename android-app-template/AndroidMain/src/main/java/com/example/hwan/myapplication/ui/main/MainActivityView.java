/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.example.hwan.myapplication.ui.main;

import com.example.hwan.myapplication.dto.CounterDto;
import com.example.hwan.myapplication.ui.base.LifecycleObservableView;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 18 - Oct - 2016
 */
interface MainActivityView extends LifecycleObservableView {
    void showCounterResult(final CounterDto counter);

    void showCounterError(final Throwable exception);
}
