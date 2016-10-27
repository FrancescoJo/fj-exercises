/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.fj.android.template.ui.base;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fj.android.template.BuildConfig;
import com.fj.android.template.util.logging.LogFactory;
import com.fj.android.template.util.logging.Logger;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 01 - Sep - 2016
 */
public class BaseFragment extends Fragment {
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

    @Nullable
    @Override
    @CallSuper
    public View onCreateView(final LayoutInflater inflater, final @Nullable ViewGroup container, final @Nullable
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        logLifecycle(String.format("onCreateView:container %s, bundle %s", container, savedInstanceState));
        return null;
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
        super.onPause();
        logLifecycle("onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        logLifecycle("onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        logLifecycle("onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        logLifecycle("onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        logLifecycle("onDetach");
    }

    private void logLifecycle(final String lifecycle) {
        if (BuildConfig.DEBUG) {
            final Logger log = LogFactory.getLogger(this.getClass().getSimpleName());
            log.v("entered lifecycle: %s", lifecycle);
        }
    }
}
