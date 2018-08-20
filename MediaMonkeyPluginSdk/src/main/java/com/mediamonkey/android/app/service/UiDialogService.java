/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.app.service;

import com.mediamonkey.android.app.ui.SettingsListDialog;

import java.util.List;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 03 - Dec - 2016
 */
public interface UiDialogService {
    SettingsListDialog makeListDialog(CharSequence title, List<?> itemList, int defaultSelection);
}
