/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.util;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;

/**
 * This class is designed to relieve burdens implementing
 * {@link android.app.Application.ActivityLifecycleCallbacks}.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Aug - 2016
 */
public class ActivityLifecycleCallbacksBuilder {
    private BiConsumer<Activity, Bundle> onCreateAction;
    private Consumer<Activity>           onStartAction;
    private Consumer<Activity>           onResumeAction;
    private Consumer<Activity>           onPauseAction;
    private Consumer<Activity>           onStopAction;
    private BiConsumer<Activity, Bundle> onSaveInstanceStateAction;
    private Consumer<Activity>           onDestroyAction;

    /**
     * @param action an action to perform when <code>onActivityCreated</code> is called.
     *               DataType param <code>Bundle</code> represents <code>savedInstanceState</code> on
     *               {@link android.app.Activity#onCreate(Bundle)}.
     */
    public ActivityLifecycleCallbacksBuilder onActivityCreated(final BiConsumer<Activity, Bundle> action) {
        this.onCreateAction = action;
        return this;
    }

    public ActivityLifecycleCallbacksBuilder onActivityStarted(final Consumer<Activity> action) {
        this.onStartAction = action;
        return this;
    }

    public ActivityLifecycleCallbacksBuilder onActivityResumed(final Consumer<Activity> action) {
        this.onResumeAction = action;
        return this;
    }

    public ActivityLifecycleCallbacksBuilder onActivityPaused(final Consumer<Activity> action) {
        this.onPauseAction = action;
        return this;
    }

    public ActivityLifecycleCallbacksBuilder onActivityStopped(final Consumer<Activity> action) {
        this.onStopAction = action;
        return this;
    }

    /**
     * @param action an action to perform when <code>onActivitySaveInstanceState</code> is called.
     *               DataType param <code>Bundle</code> represents <code>outState</code> on
     *               {@link android.app.Activity#onSaveInstanceState(Bundle)}.
     */
    public ActivityLifecycleCallbacksBuilder onActivitySaveInstanceState(final BiConsumer<Activity, Bundle> action) {
        this.onSaveInstanceStateAction = action;
        return this;
    }

    public ActivityLifecycleCallbacksBuilder onActivityDestroyed(final Consumer<Activity> action) {
        this.onDestroyAction = action;
        return this;
    }

    public Application.ActivityLifecycleCallbacks build() {
        return new CallbackImpl(onCreateAction, onStartAction, onResumeAction, onPauseAction, onStopAction,
                onSaveInstanceStateAction, onDestroyAction);
    }

    private static class CallbackImpl implements Application.ActivityLifecycleCallbacks {
        private final BiConsumer<Activity, Bundle> onCreateAction;
        private final Consumer<Activity>           onStartAction;
        private final Consumer<Activity>           onResumeAction;
        private final Consumer<Activity>           onPauseAction;
        private final Consumer<Activity>           onStopAction;
        private final BiConsumer<Activity, Bundle> onSaveInstanceStateAction;
        private final Consumer<Activity>           onDestroyAction;

        // Builder internal implementation. Not visible to outside world.
        @SuppressWarnings("PMD.ExcessiveParameterList")
        /*default*/ CallbackImpl(final BiConsumer<Activity, Bundle> onCreateAction, final Consumer<Activity>
                onStartAction, final Consumer<Activity> onResumeAction, final Consumer<Activity> onPauseAction, final
        Consumer<Activity> onStopAction, final BiConsumer<Activity, Bundle> onSaveInstanceStateAction, final
        Consumer<Activity> onDestroyAction) {
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
            invokeBiConsumer(onCreateAction, activity, savedInstanceState);
        }

        @Override
        public void onActivityStarted(final Activity activity) {
            invokeConsumer(onStartAction, activity);
        }

        @Override
        public void onActivityResumed(final Activity activity) {
            invokeConsumer(onResumeAction, activity);
        }

        @Override
        public void onActivityPaused(final Activity activity) {
            invokeConsumer(onPauseAction, activity);
        }

        @Override
        public void onActivityStopped(final Activity activity) {
            invokeConsumer(onStopAction, activity);
        }

        @Override
        public void onActivitySaveInstanceState(final Activity activity, final Bundle outState) {
            invokeBiConsumer(onSaveInstanceStateAction, activity, outState);
        }

        @Override
        public void onActivityDestroyed(final Activity activity) {
            invokeConsumer(onDestroyAction, activity);
        }

        private void invokeConsumer(Consumer<Activity> consumer, Activity activity) {
            if (null == consumer) {
                return;
            }

            try {
                consumer.accept(activity);
            } catch (Exception e) {
                throw new UnsupportedOperationException("Given lifecycle action " + consumer + " has " + "an internal" +
                        " error with '" + activity + "'.", e);
            }
        }

        private void invokeBiConsumer(BiConsumer<Activity, Bundle> consumer, Activity activity, Bundle bundle) {
            if (null == consumer) {
                return;
            }

            try {
                consumer.accept(activity, bundle);
            } catch (Exception e) {
                throw new UnsupportedOperationException("Given lifecycle action " + consumer + " has " + "an internal" +
                        " error with '" + activity + "', '" + bundle + "'.", e);
            }
        }
    }
}
