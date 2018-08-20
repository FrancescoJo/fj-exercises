/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.app;

import com.mediamonkey.android.app.service.DataStoreService;
import com.mediamonkey.android.app.service.SettingsService;
import com.mediamonkey.android.app.service.UiDialogService;

/**
 * Provides MediaMonkey APIs to plugin implementation.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 29 - Nov - 2016
 */
public interface MediaMonkeyApplicationDelegate {
    SettingsService getSettingsService();

    UiDialogService getUiDialogService();

    DataStoreService getDataStoreService();

    // MessagingService?? (send non-fatal errors, send progress ...)
}
