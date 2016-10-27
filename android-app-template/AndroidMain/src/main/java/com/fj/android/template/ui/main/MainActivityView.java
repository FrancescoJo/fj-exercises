/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.fj.android.template.ui.main;

import com.fj.android.template.dto.CounterDto;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 18 - Oct - 2016
 */
interface MainActivityView {
    void showCounterResult(final CounterDto counter);

    void showCounterError(final Throwable exception);
}
