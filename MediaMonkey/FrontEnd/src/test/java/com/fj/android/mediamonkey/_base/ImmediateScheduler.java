/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.fj.android.mediamonkey._base;

import java.util.concurrent.TimeUnit;

import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 17 - Nov - 2016
 */
class ImmediateScheduler extends Scheduler {
    @Override
    public Worker createWorker() {
        return new Worker() {
            boolean isDisposed = false;

            @Override
            public Disposable schedule(Runnable run, long delay, TimeUnit unit) {
                run.run();
                isDisposed = true;
                return this;
            }

            @Override
            public void dispose() {
                // no-op
            }

            @Override
            public boolean isDisposed() {
                return isDisposed;
            }
        };
    }
}
