/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.ui.webview;

import com.fj.android.mediamonkey.annotation.PersistOnConfigChange;
import com.fj.android.mediamonkey.ui._base.BasePresenter;

import javax.inject.Inject;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 28 - Nov - 2016
 */
@PersistOnConfigChange
class CustomWebViewPresenter extends BasePresenter<CustomWebView> {
    @Inject
    CustomWebViewPresenter() { }
}
