/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.example.hwan.myapplication.util;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import rx.functions.Action1;
import rx.functions.Action2;

/**
 * This class is designed to relieve burdens implementing
 * {@link android.app.Application.ActivityLifecycleCallbacks}.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Aug - 2016
 */
public class ActivityLifecycleCallbacksBuilder {
    private Action2<Activity, Bundle> onCreateAction;
    private Action1<Activity>         onStartAction;
    private Action1<Activity>         onResumeAction;
    private Action1<Activity>         onPauseAction;
    private Action1<Activity>         onStopAction;
    private Action2<Activity, Bundle> onSaveInstanceStateAction;
    private Action1<Activity>         onDestroyAction;

    /**
     * @param action an action to perform when <code>onActivityCreated</code> is called.
     *               Type param <code>Bundle</code> represents <code>savedInstanceState</code> on
     *               {@link android.app.Activity#onCreate(Bundle)}.
     */
    public ActivityLifecycleCallbacksBuilder onActivityCreated(
            final Action2<Activity, Bundle> action) {
        this.onCreateAction = action;
        return this;
    }

    public ActivityLifecycleCallbacksBuilder onActivityStarted(final Action1<Activity> action) {
        this.onStartAction = action;
        return this;
    }

    public ActivityLifecycleCallbacksBuilder onActivityResumed(final Action1<Activity> action) {
        this.onResumeAction = action;
        return this;
    }

    public ActivityLifecycleCallbacksBuilder onActivityPaused(final Action1<Activity> action) {
        this.onPauseAction = action;
        return this;
    }

    public ActivityLifecycleCallbacksBuilder onActivityStopped(final Action1<Activity> action) {
        this.onStopAction = action;
        return this;
    }

    /**
     * @param action an action to perform when <code>onActivitySaveInstanceState</code> is called.
     *               Type param <code>Bundle</code> represents <code>outState</code> on
     *               {@link android.app.Activity#onSaveInstanceState(Bundle)}.
     */
    public ActivityLifecycleCallbacksBuilder onActivitySaveInstanceState(
            final Action2<Activity, Bundle> action) {
        this.onSaveInstanceStateAction = action;
        return this;
    }

    public ActivityLifecycleCallbacksBuilder onActivityDestroyed(final Action1<Activity> action) {
        this.onDestroyAction = action;
        return this;
    }

    public Application.ActivityLifecycleCallbacks build() {
        return new CallbackImpl(onCreateAction, onStartAction, onResumeAction,
                                onPauseAction, onStopAction, onSaveInstanceStateAction,
                                onDestroyAction);
    }

    private static class CallbackImpl implements Application.ActivityLifecycleCallbacks {
        private final Action2<Activity, Bundle> onCreateAction;
        private final Action1<Activity>         onStartAction;
        private final Action1<Activity>         onResumeAction;
        private final Action1<Activity>         onPauseAction;
        private final Action1<Activity>         onStopAction;
        private final Action2<Activity, Bundle> onSaveInstanceStateAction;
        private final Action1<Activity>         onDestroyAction;

        // Builder internal implementation. Not visible to outside world.
        @SuppressWarnings("PMD.ExcessiveParameterList")
        /*default*/ CallbackImpl(final Action2<Activity, Bundle> onCreateAction,
                                 final Action1<Activity> onStartAction,
                                 final Action1<Activity> onResumeAction,
                                 final Action1<Activity> onPauseAction,
                                 final Action1<Activity> onStopAction,
                                 final Action2<Activity, Bundle> onSaveInstanceStateAction,
                                 final Action1<Activity> onDestroyAction) {
            this.onCreateAction = onCreateAction;
            this.onStartAction = onStartAction;
            this.onResumeAction = onResumeAction;
            this.onPauseAction = onPauseAction;
            this.onStopAction = onStopAction;
            this.onSaveInstanceStateAction = onSaveInstanceStateAction;
            this.onDestroyAction = onDestroyAction;
        }

        @Override
        public void onActivityCreated(final Activity activity, final Bundle savedInstanceState) {
            if (null != onCreateAction) {
                onCreateAction.call(activity, savedInstanceState);
            }
        }

        @Override
        public void onActivityStarted(final Activity activity) {
            if (null != onStartAction) {
                onStartAction.call(activity);
            }
        }

        @Override
        public void onActivityResumed(final Activity activity) {
            if (null != onResumeAction) {
                onResumeAction.call(activity);
            }
        }

        @Override
        public void onActivityPaused(final Activity activity) {
            if (null != onPauseAction) {
                onPauseAction.call(activity);
            }
        }

        @Override
        public void onActivityStopped(final Activity activity) {
            if (null != onStopAction) {
                onStopAction.call(activity);
            }
        }

        @Override
        public void onActivitySaveInstanceState(final Activity activity, final Bundle outState) {
            if (null != onSaveInstanceStateAction) {
                onSaveInstanceStateAction.call(activity, outState);
            }
        }

        @Override
        public void onActivityDestroyed(final Activity activity) {
            if (null != onDestroyAction) {
                onDestroyAction.call(activity);
            }
        }
    }
}
