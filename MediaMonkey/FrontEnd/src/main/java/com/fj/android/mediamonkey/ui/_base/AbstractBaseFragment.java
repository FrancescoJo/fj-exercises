/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.ui._base;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.fj.android.mediamonkey.BuildConfig;

import timber.log.Timber;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 01 - Sep - 2016
 */
public abstract class AbstractBaseFragment extends Fragment {
    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        logLifecycle(String.format("onAttach:context %s", context));
    }

    @Override
    public void onCreate(final @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logLifecycle(String.format("onCreate:bundle %s", savedInstanceState));
    }

    @Override
    public void onActivityCreated(final @Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        logLifecycle(String.format("onActivityCreated:bundle %s", savedInstanceState));
    }

    @Override
    public void onStart() {
        super.onStart();
        logLifecycle("onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        logLifecycle("onResume");
    }

    @Override
    public void onPause() {
        logLifecycle("onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        logLifecycle("onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        logLifecycle("onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        logLifecycle("onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        logLifecycle("onDetach");
        super.onDetach();
    }

    /**
     * @throws UnsupportedOperationException if this fragment is not attached to {@link com.fj.android.mediamonkey.ui._base.AbstractBaseActivity}
     * @throws IllegalStateException         when this method is called before {@link android.app.Fragment#onActivityCreated(Bundle)}.
     */
    protected <T extends BasePresenter> T getPresenter(Class<T> presenterClass) throws UnsupportedOperationException, IllegalStateException {
        Activity activity = getActivity();
        if (null == activity) {
            throw new IllegalStateException("getPresenter() is called before parent activity is injected to this fragment.");
        }

        if (!(activity instanceof AbstractBaseActivity)) {
            throw new UnsupportedOperationException(activity.getClass().getName() + " is not presenter supporting Activity.");
        }

        AbstractBaseActivity presenterActivity = (AbstractBaseActivity) activity;
        for (BasePresenter presenter : presenterActivity.getPresenters()) {
            if (presenter.getClass() == presenterClass) {
                @SuppressWarnings("unchecked")
                T presenterT = (T) presenter;
                return presenterT;
            }
        }

        return null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        logLifecycle(String.format("onSaveInstanceState:bundle %s", outState));
    }

    private void logLifecycle(final String lifecycle) {
        if (BuildConfig.DEBUG) {
            Timber.v("entered lifecycle: %s", lifecycle);
        }
    }
}
