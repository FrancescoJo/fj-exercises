/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.util.io;

import android.database.Cursor;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import dalvik.system.DexFile;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 24 - Nov - 2016
 */
public final class IOUtils {
    private static final int DEFAULT_BUFFER_SIZE = 8 * 1024;

    public static void closeQuietly(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (IOException ignore) {
            }
        }
    }

    public static void closeQuietly(DexFile dexFile) {
        if (null != dexFile) {
            try {
                dexFile.close();
            } catch (IOException ignore) {
            }
        }
    }

    public static void closeQuietly(Cursor cursor) {
        if (null != cursor) {
            try {
                cursor.close();
            } catch (Exception ignore) {
            }
        }
    }

    public static String inputStreamAsString(InputStream is) throws IOException {
        int contentSize = Math.min(is.available(), DEFAULT_BUFFER_SIZE);
        ByteArrayOutputStream baos = new ByteArrayOutputStream(contentSize);
        byte[] buf = new byte[DEFAULT_BUFFER_SIZE];
        int nRead;
        while ((nRead = is.read(buf)) != -1) {
            baos.write(buf, 0, nRead);
        }

        return new String(baos.toByteArray());
    }

    private IOUtils() { }
}
