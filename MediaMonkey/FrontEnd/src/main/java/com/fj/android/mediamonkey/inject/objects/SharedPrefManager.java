/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.inject.objects;

import android.content.Context;

import com.fj.android.mediamonkey.dao.sharedpref.AppConfigPref;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 22 - Nov - 2016
 */
@Singleton
public class SharedPrefManager {
    private AppConfigPref appConfigPref;

    @Inject
    public SharedPrefManager(Context context) {
        this.appConfigPref = new AppConfigPref(context);
    }

    public AppConfigPref getAppConfig() {
        return appConfigPref;
    }
}
