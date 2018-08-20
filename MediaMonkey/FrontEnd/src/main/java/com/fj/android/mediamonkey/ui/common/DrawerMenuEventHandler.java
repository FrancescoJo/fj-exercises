/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.ui.common;

import android.app.Activity;
import android.view.MenuItem;

/**
 * Common stateless drawer menu event handler.
 * Implementations are diversed into DEBUG/RELEASE mode.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 01 - Jan - 2017
 */
public final class DrawerMenuEventHandler {
    public static void onOptionsItemSelected(final Activity menuActivity, final MenuItem item) {
        DrawerMenuEventHandlerImpl.onOptionsItemSelected(menuActivity, item);
    }

    private DrawerMenuEventHandler() { }
}
