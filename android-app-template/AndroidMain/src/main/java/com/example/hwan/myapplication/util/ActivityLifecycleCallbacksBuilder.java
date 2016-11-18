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
 * This class is designed to relieve burdens implementing {@link android.app.Application.ActivityLifecycleCallbacks}.
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
     * @param action    an action to perform when <code>onActivityCreated</code> is called.
     *                  Type param <code>Bundle</code> represents <code>savedInstanceState</code> on
     *                  {@link android.app.Activity#onCreate(Bundle)}.
     */
    public ActivityLifecycleCallbacksBuilder onActivityCreated(Action2<Activity, Bundle> action) {
        this.onCreateAction = action;
        return this;
    }

    public ActivityLifecycleCallbacksBuilder onActivityStarted(Action1<Activity> action) {
        this.onStartAction = action;
        return this;
    }

    public ActivityLifecycleCallbacksBuilder onActivityResumed(Action1<Activity> action) {
        this.onResumeAction = action;
        return this;
    }

    public ActivityLifecycleCallbacksBuilder onActivityPaused(Action1<Activity> action) {
        this.onPauseAction = action;
        return this;
    }

    public ActivityLifecycleCallbacksBuilder onActivityStopped(Action1<Activity> action) {
        this.onStopAction = action;
        return this;
    }

    /**
     * @param action    an action to perform when <code>onActivitySaveInstanceState</code> is called.
     *                  Type param <code>Bundle</code> represents <code>outState</code> on
     *                  {@link android.app.Activity#onSaveInstanceState(Bundle)}.
     */
    public ActivityLifecycleCallbacksBuilder onActivitySaveInstanceState(Action2<Activity, Bundle> action) {
        this.onSaveInstanceStateAction = action;
        return this;
    }

    public ActivityLifecycleCallbacksBuilder onActivityDestroyed(Action1<Activity> action) {
        this.onDestroyAction = action;
        return this;
    }

    public Application.ActivityLifecycleCallbacks build() {
        return new CallbackImpl(onCreateAction, onStartAction, onResumeAction,
                onPauseAction, onStopAction, onSaveInstanceStateAction, onDestroyAction);
    }

    private static class CallbackImpl implements Application.ActivityLifecycleCallbacks {
        private final Action2<Activity, Bundle> onCreateAction;
        private final Action1<Activity>         onStartAction;
        private final Action1<Activity>         onResumeAction;
        private final Action1<Activity>         onPauseAction;
        private final Action1<Activity>         onStopAction;
        private final Action2<Activity, Bundle> onSaveInstanceStateAction;
        private final Action1<Activity>         onDestroyAction;

        /*default*/ CallbackImpl(Action2<Activity, Bundle> onCreateAction,
                                 Action1<Activity> onStartAction,
                                 Action1<Activity> onResumeAction,
                                 Action1<Activity> onPauseAction,
                                 Action1<Activity> onStopAction,
                                 Action2<Activity, Bundle> onSaveInstanceStateAction,
                                 Action1<Activity> onDestroyAction) {
            this.onCreateAction            = onCreateAction;
            this.onStartAction             = onStartAction;
            this.onResumeAction            = onResumeAction;
            this.onPauseAction             = onPauseAction;
            this.onStopAction              = onStopAction;
            this.onSaveInstanceStateAction = onSaveInstanceStateAction;
            this.onDestroyAction           = onDestroyAction;
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            if (null != onCreateAction) {
                onCreateAction.call(activity, savedInstanceState);
            }
        }

        @Override
        public void onActivityStarted(Activity activity) {
            if (null != onStartAction) {
                onStartAction.call(activity);
            }
        }

        @Override
        public void onActivityResumed(Activity activity) {
            if (null != onResumeAction) {
                onResumeAction.call(activity);
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {
            if (null != onPauseAction) {
                onPauseAction.call(activity);
            }
        }

        @Override
        public void onActivityStopped(Activity activity) {
            if (null != onStopAction) {
                onStopAction.call(activity);
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            if (null != onSaveInstanceStateAction) {
                onSaveInstanceStateAction.call(activity, outState);
            }
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            if (null != onDestroyAction) {
                onDestroyAction.call(activity);
            }
        }
    }
}
