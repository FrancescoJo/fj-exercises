/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.app;

import com.fj.android.mediamonkey.dto.PluginManifestDto;
import com.fj.android.mediamonkey.inject.objects.RxEventBus;
import com.mediamonkey.android.app.service.DataStoreService;
import com.mediamonkey.android.app.service.SettingsService;
import com.mediamonkey.android.app.service.SettingsServiceImpl;
import com.mediamonkey.android.app.service.UiDialogService;
import com.mediamonkey.android.app.service.UiDialogServiceImpl;
import com.mediamonkey.android.app.service.datastore.DataStoreServiceImpl;

/**
 * Application delegation implementation, to be injected to plugins.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 29 - Nov - 2016
 */
public class AppDelegateImpl implements MediaMonkeyApplicationDelegate {
    private PluginManifestDto manifest;
    private RxEventBus        eventBus;
    private SettingsService   settingsService;
    private UiDialogService   uiDialogService;
    private DataStoreService  dataStoreService;

    public AppDelegateImpl(PluginManifestDto manifest, RxEventBus eventBus) {
        this.manifest           = manifest;
        this.eventBus           = eventBus;
    }

    @Override
    public SettingsService getSettingsService() {
        if (null == settingsService) {
            synchronized (AppDelegateImpl.class) {
                this.settingsService = new SettingsServiceImpl(manifest, eventBus);
            }
        }

        return settingsService;
    }

    @Override
    public UiDialogService getUiDialogService() {
        if (null == uiDialogService) {
            synchronized (AppDelegateImpl.class) {
                this.uiDialogService = new UiDialogServiceImpl(eventBus);
            }
        }

        return uiDialogService;
    }

    @Override
    public DataStoreService getDataStoreService() {
        if (null == dataStoreService) {
            synchronized (AppDelegateImpl.class) {
                this.dataStoreService = new DataStoreServiceImpl(manifest);
            }
        }

        return dataStoreService;
    }
}
