/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey;

import android.app.Application;
import android.os.StrictMode;
import android.support.annotation.VisibleForTesting;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.fj.android.mediamonkey.inject.component.AppComponent;
import com.fj.android.mediamonkey.inject.component.DaggerAppComponent;
import com.fj.android.mediamonkey.inject.module.AppModule;
import com.squareup.leakcanary.LeakCanary;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import timber.log.Timber;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Aug - 2016
 */
public class MediaMonkeyApplication extends Application {
    private static MediaMonkeyApplication instance;

    private AppComponent appComponent;

    public static MediaMonkeyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setApplication(this);

        if (BuildConfig.DEBUG) {
            if (LeakCanary.isInAnalyzerProcess(this)) {
                // This process is dedicated to LeakCanary for heap analysis.
                // You should not init your app in this process.
                return;
            }
            LeakCanary.install(this);

            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().build());
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().build());
        }
        installTimberLogger();

        Fresco.initialize(this);
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule())
                .build();
    }

    private void installTimberLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } // else { Timber.plant(new TimberLogComposition()); }
    }

    /*
     * This idiom is generally wrong in Java, but OK in Android since
     * Application is initialised before all other classes and running
     * in singleton by framework.
     */
    @SuppressFBWarnings("ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD")
    @VisibleForTesting
    protected void setApplication(final MediaMonkeyApplication application) {
        MediaMonkeyApplication.instance = application;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @VisibleForTesting
    public void setAppComponent(final AppComponent component) {
        this.appComponent = component;
    }
}
