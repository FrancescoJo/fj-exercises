/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.fj.android.template.util.logging;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import io.reactivex.functions.Function;

import java.lang.ref.SoftReference;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 01 - Sep - 2016
 */
@SuppressWarnings("PMD.MoreThanOneLogger")  // PMD false positive
public final class LogFactory {
    @VisibleForTesting
    @SuppressWarnings("WeakerAccess")
    /*default*/ static final Function<String, Logger> DEFAULT_ANDROID_LOGGER = new Function<String, Logger>() {
        @Override
        public Logger apply(final String loggerName) throws Exception {
            return new AndroidLoggerForDebug(loggerName);
        }
    };

    private static final int MAXIMUM_LOGGERS_SIZE = 20;

    @VisibleForTesting
    @SuppressWarnings("WeakerAccess")
    /*default*/ static final LruCache<String, SoftReference<Logger>> LOGGERS         = new LruCache<>
            (MAXIMUM_LOGGERS_SIZE);
    private static           Function<String, Logger>                instanceFactory = DEFAULT_ANDROID_LOGGER;

    private LogFactory() {
    }

    public static Logger getLogger(final String tagName) {
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

        final Logger l = ref.get();
        if (null == l) {
            return newLogger(tagName);
        } else {
            return l;
        }
    }

    @VisibleForTesting
    @SuppressWarnings("WeakerAccess")
    /*default*/ static Function<String, Logger> getLoggerFactory() {
        return LogFactory.instanceFactory;
    }

    @VisibleForTesting
    @SuppressWarnings("WeakerAccess")
    /*default*/ static void setLoggerFactory(final @NonNull Function<String, Logger> instanceMaker) {
        LogFactory.instanceFactory = instanceMaker;
    }

    private static Logger newLogger(final String tagName) {
        final Logger log;
        try {
            log = instanceFactory.apply(tagName);
        } catch (Exception e) {
            throw new UnsupportedOperationException("Given InstanceFactory " + instanceFactory + " has an internal" +
                    "error with '" + tagName + "'.", e);
        }
        synchronized (LOGGERS) {
            LOGGERS.put(tagName, new SoftReference<>(log));
        }

        return log;
    }
}
