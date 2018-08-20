/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.ui.filepicker;

import android.content.res.Resources;
import android.os.Build;
import android.os.Environment;
import android.os.LocaleList;
import android.support.annotation.IntDef;

import com.fj.android.mediamonkey.MediaMonkeyApplication;
import com.fj.android.mediamonkey.R;
import com.fj.android.mediamonkey.util.ResourceUtils;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 23 - Nov - 2016
 */
class PluginPkgVo {
    private @Type int type;
    private File      file;
    private String    name;
    private boolean   isDirectory;

    void setFile(File file) {
        this.file = file;
        this.name = file.getName();
        this.isDirectory = file.isDirectory();
        this.type = setTypeByFile(file);
    }

    @Type int getType() {
        return type;
    }

    File getFile() {
        return file;
    }

    CharSequence getFileName() {
        return name;
    }

    CharSequence getDateAsText() {
        if (isDirectory) {
            return "";
        }

        Locale currentLocale;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            @SuppressWarnings("deprecation")
            Locale locale = MediaMonkeyApplication.getInstance().getResources().getConfiguration().locale;
            currentLocale = locale;
        } else {
            Resources res = MediaMonkeyApplication.getInstance().getResources();
            LocaleList locales = res.getConfiguration().getLocales();
            currentLocale = locales.getFirstMatch(res.getAssets().getLocales());
        }

        DateFormat format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, currentLocale);
        return format.format(new Date(file.lastModified()));
    }

    boolean isDirectory() {
        return isDirectory;
    }

    private @Type int setTypeByFile(File file) {
        if (file.isDirectory()) {
            return Type.DIRECTORY;
        } else {
            return Type.CONTAINER;
        }
    }

    static PluginPkgVo createAsParent(File file) {
        PluginPkgVo vo = new PluginPkgVo();
        vo.setFile(file);
        vo.name = ResourceUtils.getString(R.string.text_parent_directory);
        vo.type = Type.DIRECTORY_PARENT;

        return vo;
    }

    static PluginPkgVo createAsExternal() {
        PluginPkgVo vo = new PluginPkgVo();
        vo.setFile(Environment.getExternalStorageDirectory());
        vo.name = ResourceUtils.getString(R.string.text_external_storage);
        vo.type = Type.DIRECTORY_EXTERNAL;

        return vo;
    }

    @IntDef({
        Type.DIRECTORY,
        Type.DIRECTORY_PARENT,
        Type.DIRECTORY_EXTERNAL,
        Type.CONTAINER
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface Type {
        int DIRECTORY = 1;
        int DIRECTORY_PARENT = 2;
        int DIRECTORY_EXTERNAL = 3;
        int CONTAINER = 16;
    }
}
