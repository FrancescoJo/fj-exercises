/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.plugin;

import android.content.Context;
import android.support.annotation.NonNull;

import com.fj.android.mediamonkey.BuildConfig;
import com.fj.android.mediamonkey.Constants;
import com.fj.android.mediamonkey.MediaMonkeyApplication;
import com.fj.android.mediamonkey.R;
import com.fj.android.mediamonkey.dao.sharedpref.AppConfigPref;
import com.fj.android.mediamonkey.dto.PluginManifestDto;
import com.fj.android.mediamonkey.inject.component.AppComponent;
import com.fj.android.mediamonkey.inject.objects.RxEventBus;
import com.fj.android.mediamonkey.util.FileSizeUnit;
import com.fj.android.mediamonkey.util.PlatformUtils;
import com.fj.android.mediamonkey.util.ResourceUtils;
import com.fj.android.mediamonkey.util.io.FileUtils;
import com.fj.android.mediamonkey.util.io.IOUtils;
import com.mediamonkey.android.app.AppDelegateImpl;
import com.mediamonkey.android.lib.dto.VersionDto;
import com.mediamonkey.android.plugin.MediaMonkeyPlugin;
import com.mediamonkey.android.plugin.PluginAccessHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.inject.Inject;

import dalvik.system.DexFile;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import mmplugin.marumaru.MediaMonkeyPluginImpl;
import timber.log.Timber;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Nov - 2016
 */
public class PluginManager {
    private static final String BUILD_CONFIG_FILE        = "BuildConfig";
    private static final String AAPT_R_FILE              = "R";
    private static final long   DEFAULT_BUFFER_SIZE      = 16 * 1024;
    private static final File   PLUGIN_INSTALLATION_DIR  = new File(
            MediaMonkeyApplication.getInstance().getCacheDir(), "plugins"
    );
    private static final File   DEX_OPT_DIR              = new File(
            MediaMonkeyApplication.getInstance().getCacheDir(), "dexopt"
    );

    @Inject PluginAccessHelper pluginHelper;
    @Inject Context            appContext;
    @Inject AppConfigPref      appConfigPref;
    @Inject RxEventBus         eventBus;

    @Inject
    public PluginManager(MediaMonkeyApplication application) {
        this.appContext    = application;
        AppComponent appComponent = application.getAppComponent();
        this.pluginHelper  = appComponent.pluginAccessHelper();
        this.appConfigPref = appComponent.sharedPrefMgr().getAppConfig();
        this.eventBus      = appComponent.eventBus();
    }

    public Observable<PluginAccessHelper> loadLastUsedPlugin() {
        return Observable.create(new ObservableOnSubscribe<PluginAccessHelper>() {
            @Override
            public void subscribe(ObservableEmitter<PluginAccessHelper> pluginAccessHelperEmitter) throws Exception {
                // region XXX: THIS IS PoC CODE (TO BE DELETED!!)
                Timber.w("DETOURING PRODUCTION CODE FOR STUB PoC!!");
                FileInputStream fis = new FileInputStream("/mnt/sdcard/mmstub.xml");
                PluginManifestDto manifest = PluginManifestDto.create(fis);
                fis.close();
                pluginHelper.setManifest(manifest);
                pluginHelper.setDexFile(null);
                pluginHelper.setPluginInstance(new MediaMonkeyPluginImpl());
                pluginHelper.sendOnCreate(appContext, new AppDelegateImpl(manifest, eventBus));
                pluginAccessHelperEmitter.onNext(pluginHelper);
                pluginAccessHelperEmitter.onComplete();
                // endregion

//                File lastUsedPackage = new File(appConfigPref.getLastUsedPluginPackage());
//                if (!lastUsedPackage.exists()) {
//                    LOG.d("Last used package file is not exist on %s", lastUsedPackage.getAbsolutePath());
//                    throw new PluginException("Unable to load " + lastUsedPackage.getAbsolutePath());
//                }
//                LOG.v("Loading last used package %s", lastUsedPackage.getAbsolutePath());
//
//                PluginManifestDto manifest;
//                try {
//                    manifest = readManifestFromZip(lastUsedPackage);
//                    PluginManager.this.pluginHelper = loadPluginClass(pluginHelper, manifest);
//                    pluginAccessHelperEmitter.onNext(pluginHelper);
//                    LOG.v("Loaded last used package %s", lastUsedPackage.getAbsolutePath());
//                } catch (IOException e) {
//                    LOG.d("Last used package file is corrupted: %s", lastUsedPackage.getAbsolutePath());
//                    appConfigPref.setLastUsedPluginPackage("");
//                    throw e;
//                } finally {
//                    pluginAccessHelperEmitter.onComplete();
//                }
            }
        });
    }

    /**
     * Returns an observable that emits plugin instance. Obtained observable
     * will emit these exceptions on error:
     * <ul>
     * <li>{@link PluginException} if same plugin is already installed or plugin class are malformed</li>
     * <li>{@link IOException}     on general I/O failure while installing given plugin package</li>
     * </ul>
     *
     * @param manifest Plugin manifest that user requested to install
     * @param force    decision flag to check prerequisites
     */
    public Observable<PluginAccessHelper> loadPlugin(final PluginManifestDto manifest, final boolean force) {
        return Observable.create(new ObservableOnSubscribe<PluginAccessHelper>() {
            @Override
            public void subscribe(ObservableEmitter<PluginAccessHelper> e) throws Exception {
                // region XXX: THIS IS PoC CODE (TO BE DELETED!!)
                Timber.w("DETOURING PRODUCTION CODE FOR STUB PoC!!");
                FileInputStream fis = new FileInputStream("/mnt/sdcard/mmstub.xml");
                PluginManifestDto manifest = PluginManifestDto.create(fis);
                fis.close();
                pluginHelper.setManifest(manifest);
                pluginHelper.setDexFile(null);
                pluginHelper.setPluginInstance(new MediaMonkeyPluginImpl());
                pluginHelper.sendOnCreate(appContext, new AppDelegateImpl(manifest, eventBus));
                e.onNext(pluginHelper);
                e.onComplete();
                // endregion

//                    boolean isInstallationRequired = force;
//                    if (!force) {
//                        isInstallationRequired = checkInstallRequired(manifest);
//                    }
//
//                    if (isInstallationRequired) {
//                        installPlugin(manifest);
//                    }
//
//                    PluginManager.this.pluginHelper = loadPluginClass(pluginHelper, manifest);
//                    e.onNext(pluginHelper);
//                    e.onComplete();
            }
        });
    }

    private boolean checkInstallRequired(PluginManifestDto manifest) throws PluginException, IOException {
        VersionDto oldVersion = getInstalledVersion(manifest);
        if (null == oldVersion) {
            return true;
        }

        VersionDto newVersion = manifest.getVersion();
        int compareResult = newVersion.compareTo(oldVersion);

        if (compareResult < 0) {
            PluginException ex = new PluginException("Plugin is already installed");
            ex.setOldVersion(oldVersion);
            throw ex;
        }

        // Overwrite on same or older version
        return true;
    }

    private VersionDto getInstalledVersion(PluginManifestDto manifest) throws IOException {
        File pkgFile = guessInstalledPackageFile(manifest);
        if (!pkgFile.exists()) {
            Timber.d("Plugin package is not found on %s", pkgFile.getAbsolutePath());
            return null;
        } else {
            try {
                PluginManifestDto installedManifest = readManifestFromZip(pkgFile);
                Timber.d("Pre-installed package version: %s", installedManifest.getVersion());
                return installedManifest.getVersion();
            } catch (IllegalArgumentException ex) {
                Timber.d(ex, "Error while parsing pre-installed plugin manifest in %s", pkgFile.getAbsolutePath());
                return null;
            }
        }
    }

    private void installPlugin(PluginManifestDto manifest) throws IOException {
        File source = manifest.getPkgFile();
        File target = guessInstalledPackageFile(manifest);
        Locale currentLocale = PlatformUtils.getCurrentLocale();
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            // region Phase 1: Check size
            if (PLUGIN_INSTALLATION_DIR.exists() && !PLUGIN_INSTALLATION_DIR.isDirectory()) {
                if (!PLUGIN_INSTALLATION_DIR.delete()) {
                    Timber.w("Unable to clean path: %s", PLUGIN_INSTALLATION_DIR);
                }
            }

            if (!PLUGIN_INSTALLATION_DIR.exists()) {
                Timber.v("Creating plugin installation directory: %s", PLUGIN_INSTALLATION_DIR);
                if (!PLUGIN_INSTALLATION_DIR.mkdirs()) {
                    Timber.w("Unable to make plugin installation directory: %s", PLUGIN_INSTALLATION_DIR);
                }
            }

            if (!PLUGIN_INSTALLATION_DIR.exists()) {
                Timber.w("Plugin installation directory is not found: %s", PLUGIN_INSTALLATION_DIR);
            }

            long availableSize = PLUGIN_INSTALLATION_DIR.getUsableSpace();
            long requiredSize = source.length();
            Timber.d("Available disk space: %s, required package size: %s", availableSize, requiredSize);
            if (availableSize < requiredSize) {
                throw new IOException(appContext.getString(R.string.err_insufficient_storage_space,
                        FileSizeUnit.getBest(requiredSize).asString(requiredSize, currentLocale)));
            }
            // endregion

            // region Phase 2: Copy into plugin directory
            fis = new FileInputStream(source);
            fos = new FileOutputStream(target);
            byte[] buffer = new byte[(int) DEFAULT_BUFFER_SIZE];
            int nRead;
            while ((nRead = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, nRead);
            }
            Timber.d("Plugin file copied into %s", target.getAbsolutePath());
            // endregion
        } finally {
            IOUtils.closeQuietly(fos);
            IOUtils.closeQuietly(fis);
        }
    }

    private File guessInstalledPackageFile(PluginManifestDto manifest) {
        VersionDto version = manifest.getVersion();
        String versionPostfix = version.getMajorVersion() + "." +
                version.getMinorVersion() + "." + version.getBuildNumber();

        return new File(PLUGIN_INSTALLATION_DIR, manifest.getPackageName() + "-" +
                versionPostfix + Constants.PLUGIN_PACKAGE_SUFFIX);
    }

    private PluginAccessHelper loadPluginClass(@NonNull PluginAccessHelper helper,
                                               PluginManifestDto manifest) throws IOException {
        if (helper.isPluginLoaded()) {
            helper.cleanUp();
        }

        String packageName = manifest.getPackageName();
        File dexOptDir = new File(DEX_OPT_DIR, packageName);
        if (!dexOptDir.exists() && !dexOptDir.mkdirs()) {
            throw new IOException(ResourceUtils.getString(R.string.err_unable_to_access_storage));
        }

        if (!dexOptDir.isDirectory()) {
            if (!dexOptDir.delete() || !dexOptDir.mkdirs()) {
                throw new IOException(ResourceUtils.getString(R.string.err_unable_to_access_storage));
            }
        }

        File containerFile = guessInstalledPackageFile(manifest);
        if (!containerFile.exists()) {
            throw new IOException(ResourceUtils.getString(R.string.err_unable_to_access_storage));
        }

        File dexOptFile = new File(dexOptDir, containerFile.getName() + ".odex");
        if (!dexOptFile.exists() && !dexOptFile.createNewFile()) {
            throw new IOException(ResourceUtils.getString(R.string.err_unable_to_access_storage));
        }
        Timber.d("DexOpt file     : %s", dexOptFile.getAbsolutePath());
        Timber.d("Container path  : %s", containerFile.getAbsolutePath());

        Class<MediaMonkeyPlugin> pluginClass = MediaMonkeyPlugin.class;
        MediaMonkeyPlugin pluginInstance = null;
        DexFile dex = null;
        try {
            Timber.v("Loading container file %s", containerFile.getAbsolutePath());
            dex = DexFile.loadDex(containerFile.getAbsolutePath(), dexOptFile.getAbsolutePath(), 0);
            Enumeration<String> entries = dex.entries();
            if (!entries.hasMoreElements()) {
                throw new IOException("Malformed plugin executable");
            }

            final String[] skipNames = new String[] {
                    packageName + "." + BUILD_CONFIG_FILE, packageName + "." + AAPT_R_FILE
            };
            String entry = entries.nextElement();
            for (; entries.hasMoreElements(); entry = entries.nextElement()) {
                if (!entry.startsWith(packageName)) {
                    Timber.v("Skipping non-package class %s", entry);
                    continue;
                }

                if (isAndroidBuildClass(skipNames, entry)) {
                    continue;
                }

                Timber.v("Loading class entry %s", entry);
                Class<?> klass;
                try {
                    klass = dex.loadClass(entry, Thread.currentThread().getContextClassLoader());
                    Timber.v("Loaded class %s", klass);
                    if (klass.getSuperclass().equals(pluginClass)) {
                        Timber.d("Trying to acquire instance of %s", pluginClass);
                        pluginInstance = (MediaMonkeyPlugin) klass.newInstance();
                        Timber.d("Instantiation successful: %s", pluginInstance);
                    }
                } catch(Exception e) {
                    if (BuildConfig.DEBUG) {
                        e.printStackTrace();
                    }

                    // ReflectiveOperationException e (API Level+ 19)
                    if (e instanceof IllegalAccessException || e instanceof InstantiationException) {
                        throw new IOException("Internal programme logic in " + packageName, e);
                    } else {
                        throw new IOException("Unable to load " + entry + " for unknown reason", e);
                    }
                } catch(IncompatibleClassChangeError e) {
                    throw new IOException("Plugin executable format is incompatible", e);
                }
            }

            Timber.v("Memorising last used package %s", containerFile.getAbsolutePath());
            appConfigPref.setLastUsedPluginPackage(containerFile.getAbsolutePath());
            helper.setManifest(manifest);
            helper.setDexFile(dex);
            helper.setPluginInstance(pluginInstance);
            helper.sendOnCreate(appContext, new AppDelegateImpl(manifest, eventBus));
            return helper;
        } finally {
            IOUtils.closeQuietly(dex);
        }
    }

    private boolean isAndroidBuildClass(String[] skipNames, String className) {
        for (String skippingName : skipNames) {
            if (className.startsWith(skippingName)) {
                Timber.v("Skipping android build result class %s", className);
                return true;
            }
        }

        return false;
    }

    /**
     * Returns an observable that emits plugin manifest. Obtained observable
     * will emit these exceptions on error:
     * <ul>
     * <li>{@link IllegalArgumentException} if given mmplugin file is malformed</li>
     * <li>{@link InstantiationException}   if given mmplugin file is well-formed but
     * executables are missing</li>
     * <li>{@link IOException} on general I/O failure while accessing given mmplugin file</li>
     * </ul>
     *
     * @param file Zipped plugin file
     */
    public Observable<PluginManifestDto> readPluginFile(final File file) {
        return Observable.create(new ObservableOnSubscribe<PluginManifestDto>() {
            @Override
            public void subscribe(ObservableEmitter<PluginManifestDto> e) throws Exception {
                try {
                    e.onNext(readManifestFromZip(file));
                } catch (Exception ex) {
                    e.onError(ex);
                } finally {
                    e.onComplete();
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    private PluginManifestDto readManifestFromZip(final File zipFile) throws IOException {
        FileInputStream pkgFileIs = null;
        ZipInputStream zipEntryIs = null;
        InputStream is = null;
        try {
            pkgFileIs = new FileInputStream(zipFile);
            zipEntryIs = new ZipInputStream(pkgFileIs);
            is = getFileStreamInPackage(zipEntryIs, PluginManifestDto.FILE_NAME_PKG_INFO);
            if (null == is) {
                throw new IllegalArgumentException("Malformed package file " + zipFile);
            }

            String sha1Hash = FileUtils.calculateSHA1(zipFile);
            return PluginManifestDto.create(zipFile, sha1Hash, is);
        } finally {
            IOUtils.closeQuietly(pkgFileIs);
            IOUtils.closeQuietly(zipEntryIs);
            IOUtils.closeQuietly(is);
        }
    }

    private static InputStream getFileStreamInPackage(ZipInputStream zis, String name) throws IOException {
        ZipEntry ze;
        while ((ze = zis.getNextEntry()) != null) {
            if (ze.getName().equals(name)) {
                return zis;
            }
        }

        return null;
    }
}
