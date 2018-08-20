/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.ui.filepicker;

import java.util.List;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 23 - Nov - 2016
 */
interface FilePickerView {
    void showLoading();

    void hideLoading();

    void clearList();

    boolean isContentLoaded();

    void showFileList(String path, List<PluginPkgVo> fileList);
}
