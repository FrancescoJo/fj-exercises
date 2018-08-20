/*
 * SimpleHttpServer project
 *
 * Read LICENSE file in project root for licence details.
 */
package com.francescojo.simplehttpsvr.util;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Oct - 2016
 */
public final class StringPatternUtils {
    public static String wildcard2Regex(String wildcard) {
        return "^" + wildcard.replaceAll("\\.", "\\\\.").replaceAll("\\?", ".").replaceAll("\\*", ".*") + "$";
    }

    public static String removeBackSlash(String windowsPath) {
        return windowsPath.replaceAll("\\\\", "/");
    }

    public static String replaceWindowsPath(String path) {
        if (path.contains("\\")) {
            return path.replace("\\", "/");
        }

        return path;
    }

    private StringPatternUtils() {}
}
