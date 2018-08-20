/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.lib.internal;

import android.database.Cursor;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 25 - Nov - 2016
 */
public final class MmSdkUtils {
    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(final String text) {
        String nullSafeStr;
        if (null == text) {
            nullSafeStr = "";
        } else {
            nullSafeStr = text;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return Html.fromHtml(nullSafeStr);
        } else {
            return Html.fromHtml(nullSafeStr, Html.FROM_HTML_MODE_LEGACY);
        }
    }

    public static long getLong(Cursor cursor, String schemaName, String name) {
        return cursor.getLong(indexOf(cursor, schemaName, name));
    }

    public static int getInt(Cursor cursor, String schemaName, String name) {
        return cursor.getInt(indexOf(cursor, schemaName, name));
    }

    public static String getString(Cursor cursor, String schemaName, String name, String defaultValue) {
        int columnIndex;
        try {
            columnIndex = indexOf(cursor, schemaName, name);
        } catch (IllegalArgumentException e) {
            return defaultValue;
        }

        String value = cursor.getString(columnIndex);
        return value != null ? value : defaultValue;
    }

    public static float getFloat(Cursor cursor, String schemaName, String name) {
        return cursor.getFloat(indexOf(cursor, schemaName, name));
    }

    private static int indexOf(Cursor cursor, String schemaName, String columnName) {
        return cursor.getColumnIndexOrThrow(schemaName + "_" + columnName);
    }
}
