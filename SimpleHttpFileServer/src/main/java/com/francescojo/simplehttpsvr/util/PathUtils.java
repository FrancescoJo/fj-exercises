/*
 * SimpleHttpServer project
 *
 * Read LICENSE file in project root for licence details.
 */
package com.francescojo.simplehttpsvr.util;

import com.google.common.base.Strings;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 01 - Nov - 2016
 */
public final class PathUtils {
    public static String getRelativePathOf(String relativePath) {
        if (Strings.isNullOrEmpty(relativePath)) {
            return "/";
        }

        return StringPatternUtils.replaceWindowsPath(relativePath);
    }

    private PathUtils() {}
}
