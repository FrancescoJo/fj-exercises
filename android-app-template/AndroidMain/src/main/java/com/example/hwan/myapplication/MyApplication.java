/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.example.hwan.myapplication;

import android.app.Application;
import android.os.StrictMode;

import com.example.hwan.myapplication.dagger2.component.DaggerDiComponent;
import com.example.hwan.myapplication.dagger2.component.DiComponent;
import com.example.hwan.myapplication.dagger2.module.NetworkApiModule;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Aug - 2016
 */
public class MyApplication extends Application {
    private static MyApplication instance;
    private DiComponent diComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.instance = this;

        diComponent = DaggerDiComponent.builder()
                .networkApiModule(new NetworkApiModule())
                .build();

        if (BuildConfig.DEBUG) {
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll().build());
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll().build());
        }
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public DiComponent getDiComponent() {
        return diComponent;
    }
}
