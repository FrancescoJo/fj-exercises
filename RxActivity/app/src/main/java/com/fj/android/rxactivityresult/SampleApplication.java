package com.fj.android.rxactivityresult;

import android.app.Application;
import android.os.StrictMode;

public class SampleApplication extends Application {
    private static SampleApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        // OK in Android; however bad practice in general Java
        SampleApplication.instance = this;

        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
        }
    }

    public static SampleApplication getInstance() {
        return SampleApplication.instance;
    }
}
