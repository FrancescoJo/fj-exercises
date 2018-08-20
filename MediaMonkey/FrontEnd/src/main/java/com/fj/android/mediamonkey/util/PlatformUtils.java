/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import com.fj.android.mediamonkey.MediaMonkeyApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 24 - Nov - 2016
 */
public final class PlatformUtils {
    public static boolean isPermissionGranted(String permission) {
        // Do not check under API Level < 23
        Context c = MediaMonkeyApplication.getInstance();

        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(c, permission);
    }

    public static List<String> getGrantedPermissions() {
        List<String> permissionList = null;
        Context context = MediaMonkeyApplication.getInstance();
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
            if (info.requestedPermissions != null) {
                permissionList = new ArrayList<>();
                for (String p : info.requestedPermissions) {
                    if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, p)) {
                        permissionList.add(p);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException ignore) {
        }
        return permissionList;
    }

    @SuppressWarnings("deprecation")
    public static Locale getCurrentLocale() {
        Configuration c = MediaMonkeyApplication.getInstance().getResources().getConfiguration();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return c.getLocales().get(0);
        } else {
            return c.locale;
        }
    }

    private PlatformUtils() { }
}
