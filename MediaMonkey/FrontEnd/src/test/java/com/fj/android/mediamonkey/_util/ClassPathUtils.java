/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey._util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 07 - Dec - 2016
 */
public final class ClassPathUtils {
    public static InputStream getResourceAsStream(String resourceName) throws IOException {
        String callerClassName = Thread.currentThread().getStackTrace()[2].getClassName();
        Class<?> callerClass;
        try {
            callerClass = Class.forName(callerClassName);
        } catch (ClassNotFoundException e) {
            // Class is not found in same class loader
            throw new FileNotFoundException("Given resource " + resourceName + " is not found in current context class loader.");
        }
        String packageName = callerClass.getPackage().getName();
        return getResourceAsStream(packageName, resourceName);
    }

    public static InputStream getResourceAsStream(String packageName, String resourceName) throws IOException {
        String dirName = packageName.replaceAll("\\.", "/");
        String trueResourceName = dirName + "/" + resourceName;

        URL url = ClassPathUtils.class.getClassLoader().getResource(trueResourceName);
        return url.openStream();
    }

    private ClassPathUtils() { }
}
