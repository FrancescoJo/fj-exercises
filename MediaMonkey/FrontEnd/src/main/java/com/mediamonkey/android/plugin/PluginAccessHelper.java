/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.plugin;

import android.content.Context;
import android.support.annotation.WorkerThread;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.fj.android.mediamonkey.dto.PluginManifestDto;
import com.fj.android.mediamonkey.util.io.IOUtils;
import com.mediamonkey.android.app.AppDelegateImpl;
import com.mediamonkey.android.app.service.datastore.DataStoreServiceImpl;

import java.io.File;

import javax.inject.Inject;

import dalvik.system.DexFile;
import timber.log.Timber;

/**
 * Helper class to provide easy access to package default and protected fields of
 * MediaMonkeyPlugin class.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 25 - Nov - 2016
 */
public class PluginAccessHelper {
    @Inject
    public PluginAccessHelper() { }

    private PluginManifestDto    manifest;
    private DexFile              dexFile;
    private MediaMonkeyPlugin    instance;
    private DataStoreServiceImpl dataStoreServiceImpl;

    public void setPluginInstance(MediaMonkeyPlugin pluginInstance) {
        this.instance = pluginInstance;
    }

    public void setDexFile(DexFile dexFile) {
        this.dexFile = dexFile;
    }

    public void setManifest(PluginManifestDto manifest) {
        this.manifest = manifest;
    }

    public MediaMonkeyPlugin getPluginInstance() {
        return instance;
    }

    public PluginManifestDto getManifest() {
        return manifest;
    }

    public boolean isPluginLoaded() {
        return instance != null;
    }

    public boolean hasSettings() {
        return isPluginLoaded() && instance.getSettingsService() != null;
    }

    public void cleanUp() {
        if (null == instance) {
            return;
        }

        instance.onDestroy();
        IOUtils.closeQuietly(dexFile);
        if (null != dataStoreServiceImpl) {
            IOUtils.closeQuietly(dataStoreServiceImpl.getConnectedDatabase());
        }
        this.dexFile = null;
        this.manifest = null;
        this.instance = null;
    }

    @WorkerThread
    public void sendOnCreate(Context applicationContext, AppDelegateImpl appDelegate) {
        ensurePluginInjected();
        Timber.d("[sendOnCreate] dexFile : %s", dexFile);
        Timber.d("[sendOnCreate] manifest: %s", manifest);
        Timber.d("[sendOnCreate] instance: %s", instance);
        File cacheDir = new File(applicationContext.getExternalCacheDir(), manifest.getPackageName());
        Timber.d("[sendOnCreate] cache dir: %s", cacheDir);

        DiskCacheConfig config = DiskCacheConfig.newBuilder(applicationContext)
                .setBaseDirectoryPath(cacheDir)
                .build();

        ImagePipelineConfig ipConfig = ImagePipelineConfig.newBuilder(applicationContext)
                .setMainDiskCacheConfig(config)
                .build();

        // Fresco complains about this, but any other approaches to set cache dir name dynamically?
        Fresco.shutDown();
        Fresco.initialize(applicationContext, ipConfig);

        instance.onCreate(applicationContext, appDelegate);

        DataStoreInterface dsInterface = instance.getDataStoreInterface();
        if (null != dsInterface) {
            this.dataStoreServiceImpl = (DataStoreServiceImpl) appDelegate.getDataStoreService();
            dataStoreServiceImpl.onPluginRequestsDatabase(dsInterface);
        }
    }

    private void ensurePluginInjected() {
        if (null == instance) {
            throw new IllegalStateException("MediaMonkey plugin instance is not loaded.");
        }
    }
}
