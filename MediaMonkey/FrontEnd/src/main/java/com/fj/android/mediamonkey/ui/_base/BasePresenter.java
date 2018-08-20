/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.ui._base;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 18 - Oct - 2016
 */
public class BasePresenter<T> {
    private T view;

    public final void attachView(final T view) {
        this.view = view;
        onAttachView(view);
    }

    public final void detachView(boolean isConfigChanged) {
        this.view = null;
        onDetachView(isConfigChanged);
    }

    public T getView() {
        return view;
    }

    public boolean isAttached() {
        return null != view;
    }

    protected void onAttachView(final @SuppressWarnings("UnusedParameters") T view) {
        // Providing hook on attachView(T) method
    }

    protected void onDetachView(final @SuppressWarnings("UnusedParameters") boolean isConfigChanged) {
        // Providing hook on detachView() method
    }
}
