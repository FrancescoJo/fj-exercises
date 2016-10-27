/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.fj.android.template.ui.base;

import android.support.annotation.CallSuper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 18 - Nov - 2016
 */
public class AutoDisposablePresenter<T> extends BasePresenter<T> {
    private List<WeakReference<Disposable>> disposables;

    @CallSuper
    @Override
    protected void onAttachView(@SuppressWarnings("UnusedParameters") T view) {
        this.disposables = new ArrayList<>();
    }

    protected void addToAutoDisposables(Disposable disposable) {
        disposables.add(new WeakReference<>(disposable));
    }

    @CallSuper
    @Override
    protected void onDetachView(@SuppressWarnings("UnusedParameters") boolean isConfigChanged) {
        for (WeakReference<Disposable> disposableRef : disposables) {
            Disposable disposable = disposableRef.get();
            if (null == disposable || disposable.isDisposed()) {
                continue;
            }

            disposable.dispose();
        }
    }
}
