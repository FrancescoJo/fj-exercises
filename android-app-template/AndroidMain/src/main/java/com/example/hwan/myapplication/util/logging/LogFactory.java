/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.example.hwan.myapplication.util.logging;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.util.LruCache;
import android.text.TextUtils;

import java.lang.ref.SoftReference;

import rx.functions.Func1;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 01 - Sep - 2016
 */
public class LogFactory {
    @VisibleForTesting @SuppressWarnings("WeakerAccess")
    /*default*/ static final Func1<String, Logger> DEFAULT_ANDROID_LOGGER = new Func1<String, Logger>() {
        @Override
        public Logger call(String loggerName) {
            return new AndroidLoggerForDebug(loggerName);
        }
    };

    private static final int MAXIMUM_LOGGERS_SIZE = 20;

    @VisibleForTesting @SuppressWarnings("WeakerAccess")
    /*default*/ static final LruCache<String, SoftReference<Logger>> LOGGERS
            = new LruCache<>(MAXIMUM_LOGGERS_SIZE);
    private static Func1<String, Logger> instanceFactory = DEFAULT_ANDROID_LOGGER;

    public static Logger getLogger(String tagName) {
        if (TextUtils.isEmpty(tagName)) {
            throw new IllegalArgumentException("Tag name must not be empty");
        }

        synchronized (LOGGERS) {
            if (LOGGERS.get(tagName) == null) {
                return newLogger(tagName);
            }
        }

        SoftReference<Logger> ref;
        synchronized (LOGGERS) {
            ref = LOGGERS.get(tagName);
        }

        Logger l = ref.get();
        if (l != null) {
            return l;
        } else {
            return newLogger(tagName);
        }
    }

    @VisibleForTesting @SuppressWarnings("WeakerAccess")
    /*default*/ static Func1<String, Logger> getLoggerFactory() {
        return LogFactory.instanceFactory;
    }

    @VisibleForTesting @SuppressWarnings("WeakerAccess")
    /*default*/ static void setLoggerFactory(@NonNull Func1<String, Logger> loggerInstanceFactory) {
        LogFactory.instanceFactory = loggerInstanceFactory;
    }

    private static Logger newLogger(String tagName) {
        Logger log = instanceFactory.call(tagName);
        synchronized (LOGGERS) {
            LOGGERS.put(tagName, new SoftReference<>(log));
        }

        return log;
    }
}
