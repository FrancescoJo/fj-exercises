/*
 * MyAwesomeApp project template
 *
 * Distributed under no licences and no warranty.
 */
package com.fj.android.template._base;

import com.fj.android.template.BuildConfig;
import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.manifest.AndroidManifest;
import org.robolectric.res.Fs;

import java.io.File;

/**
 * Custom test runner for testing logic depend on Application#getResources()
 * <p>
 * This class is used by Robolectric. Don't inherit this class for your test fixtures!!
 * </p>
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @see
 * <a href="http://stackoverflow.com/questions/33430579/robolectric-throwing-resourcenotfound-exception">Robolectric
 * throwing ResourceNotFound exception</a>
 */
@SuppressWarnings("WeakerAccess")
public class MyRobolectricGradleTestRunner extends RobolectricTestRunner {
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
        String res = basePath + String.format("/build/intermediates/res/merged/%1$s/%2$s", BuildConfig.FLAVOR,
                BuildConfig.BUILD_TYPE);
        String asset = basePath + "/src/test/assets";
        return new AndroidManifest(Fs.fileFromPath(manifest), Fs.fileFromPath(res), Fs.fileFromPath(asset));
    }
}