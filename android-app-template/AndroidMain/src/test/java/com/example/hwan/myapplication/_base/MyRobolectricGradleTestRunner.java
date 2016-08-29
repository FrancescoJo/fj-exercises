/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.example.hwan.myapplication._base;

import com.example.hwan.myapplication.BuildConfig;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.manifest.AndroidManifest;
import org.robolectric.res.Fs;

import java.io.File;

import rx.Scheduler;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.plugins.RxJavaHooks;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.schedulers.Schedulers;

/**
 * Custom test runner for testing logic depend on Application#getResources()
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @see <a href="http://stackoverflow.com/questions/33430579/robolectric-throwing-resourcenotfound-exception">Robolectric throwing ResourceNotFound exception</a>
 */
public class MyRobolectricGradleTestRunner extends RobolectricGradleTestRunner {
    /**
     * Robolectric supported latest Android SDK version (Current: Nov. 2015)
     */
    public static final int SDK_VERSION = 21;

    public MyRobolectricGradleTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    protected AndroidManifest getAppManifest(Config config) {
        String basePath = new File(".").getAbsolutePath();
        String manifest = basePath + "/src/main/AndroidManifest.xml";
        String res = basePath + String.format("/build/intermediates/res/merged/%1$s/%2$s", BuildConfig.FLAVOR, BuildConfig.BUILD_TYPE);
        String asset = basePath + "/src/test/assets";
        return new AndroidManifest(Fs.fileFromPath(manifest), Fs.fileFromPath(res), Fs.fileFromPath(asset));
    }
}