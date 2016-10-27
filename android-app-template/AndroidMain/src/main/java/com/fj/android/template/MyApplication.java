/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.fj.android.template;

import android.app.Application;
import android.os.StrictMode;
import android.support.annotation.VisibleForTesting;
import com.fj.android.template.inject.component.AppComponent;
import com.fj.android.template.inject.component.DaggerAppComponent;
import com.fj.android.template.inject.module.AppModule;
import com.fj.android.template.inject.module.NetworkApiModule;
import com.squareup.leakcanary.LeakCanary;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Aug - 2016
 */
public class MyApplication extends Application {
    private static MyApplication instance;
    private        AppComponent  appComponent;

    public static MyApplication getInstance() {
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

        appComponent = DaggerAppComponent.builder().appModule(new AppModule()).networkApiModule(new NetworkApiModule
                ()).build();
    }

    /*
     * This idiom is generally wrong in Java, but OK in Android since
     * Application is initalisied before all other classes and running
     * in singleton by framework.
     */
    @SuppressFBWarnings("ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD")
    @VisibleForTesting
    protected void setApplication(final MyApplication application) {
        MyApplication.instance = application;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @VisibleForTesting
    public void setAppComponent(final AppComponent component) {
        this.appComponent = component;
    }
}
