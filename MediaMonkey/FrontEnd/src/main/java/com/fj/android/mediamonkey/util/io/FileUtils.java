/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.util.io;

import android.os.Build;
import android.os.FileObserver;
import android.os.StatFs;
import android.support.annotation.NonNull;

import com.fj.android.mediamonkey.util.lang.EmptyCheckUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 24 - Nov - 2016
 */
public final class FileUtils {
    private static final long DEFAULT_BUFFER_SIZE = 16 * 1024;

    public static boolean isSymbolicLink(final @NonNull File file) throws IOException {
        return !file.getCanonicalFile().equals(file.getAbsoluteFile());
    }

    /**
     * Checks whether given <code>dir</code> is empty or contains only symlinks in.
     * @param dir    A filesystem directory object represented as {@link java.io.File}
     * @return <code>true</code> if <code>dir</code> is empty or contains symlink(s) only,
     * <code>false</code> otherwise.
     */
    public static boolean isDirEmpty(final @NonNull File dir) throws IOException {
        return isDirEmpty(dir, 2);
    }

    private static boolean isDirEmpty(final @NonNull File dir, final int recursionDepth) throws IOException {
        if (!dir.isDirectory() || 0 == recursionDepth) {
            // Don't care
            return false;
        }

        File[] files = dir.listFiles();
        if (EmptyCheckUtils.isEmpty(files)) {
            return true;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                if (!isDirEmpty(file, recursionDepth - 1)) {
                    return false;
                }
            } else {
                if (!isSymbolicLink(file)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * @return SHA-1 hash of given file, <code>null</code> if <code>file</code> is inaccessible
     */
    public static String calculateSHA1(final File file) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException ignore) {
            // Unreachable here unless ran under Android
            return null;
        }
        InputStream fis = null;
        try {
            fis = new FileInputStream(file);
            int n = 0;
            byte[] buffer = new byte[8192];
            while (n != -1) {
                n = fis.read(buffer);
                if (n > 0) {
                    digest.update(buffer, 0, n);
                }
            }
        } catch (IOException e) {
            return null;
        } finally {
            IOUtils.closeQuietly(fis);
        }

        byte[] hash = digest.digest();
        return HexUtils.hex2StrLower(hash);
    }

    public static void startPathUpdateObserver(final FileObserver observer) {
        if (null == observer) {
            return;
        }

        observer.startWatching();
    }

    public static void stopPathUpdateObserver(final FileObserver observer) {
        if (null == observer) {
            return;
        }

        observer.startWatching();
    }

    @SuppressWarnings("deprecation")
    public static void copy(final String srcPath, final String destPath) throws IOException {
        File srcFile = new File(srcPath);
        if (!srcFile.exists()) {
            throw new FileNotFoundException("Given file " + srcPath + " is not found!!");
        }
        File destFile = new File(destPath);
        StatFs stat = new StatFs(destFile.getParent());
        long bytesAvailable;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            bytesAvailable = (long) stat.getBlockSize() * (long) stat.getBlockCount();
        } else {
            bytesAvailable = stat.getBlockSizeLong() * stat.getBlockCountLong();
        }

        if (srcFile.length() > bytesAvailable) {
            throw new IOException("Bytes required: " + srcFile.length() +
                " > Bytes available " + bytesAvailable + " on " + destFile.getPath() + "!!");
        }

        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(srcFile);
            fos = new FileOutputStream(destFile);
            byte[] buffer = new byte[(int) DEFAULT_BUFFER_SIZE];
            int nRead;
            while ((nRead = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, nRead);
            }
        } finally {
            IOUtils.closeQuietly(fis);
            IOUtils.closeQuietly(fos);
        }
    }

    private FileUtils() { }
}
