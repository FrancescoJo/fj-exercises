/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.ui.viewer;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Dec - 2016
 */
interface ViewerView {
    void showLoadingView();

    void hideLoadingView();

    void showContent(final WrappedContent inventoryList);

    void showError(final Throwable e);

    boolean isSysUiShowing();

    void toggleOrientation();

    void toggleSysUi();

    ViewerDataManager getDataManager();
}
