/*
 * SimpleHttpServer project
 *
 * Read LICENSE file in project root for licence details.
 */
package com.francescojo.simplehttpsvr.model;

import com.francescojo.simplehttpsvr.enums.FileSizeUnit;
import com.francescojo.simplehttpsvr.util.StringPatternUtils;

import java.text.DateFormat;
import java.util.Locale;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - Oct - 2016
 */
// All fields are accessed by template engine
@SuppressWarnings("WeakerAccess")
public class FileItem {
    public final String iconName;
    public final String name;
    public final String path;
    public final String lastModified;
    public final String size;
    public final String description;

    private FileItem(String iconName, String name, String path, long lastModified, long size, String description,
                     Locale locale) {
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, locale);

        this.iconName = iconName;
        this.name = name;
        this.path = StringPatternUtils.removeBackSlash(path);
        this.lastModified = df.format(lastModified);
        this.size = FileSizeUnit.getBest(size).asString(size, locale);
        this.description = description;
    }

    public static class Builder {
        private String iconName;
        private String name;
        private String path;
        private long   lastModified;
        private long   size;
        private String description;
        private Locale locale;

        public Builder iconName(String s) {
            this.iconName = s;
            return this;
        }

        public Builder name(String s) {
            this.name = s;
            return this;
        }

        public Builder path(String s) {
            this.path = s;
            return this;
        }

        public Builder lastModified(long num) {
            this.lastModified = num;
            return this;
        }

        public Builder size(long num) {
            this.size = num;
            return this;
        }

        public Builder description(String s) {
            this.description = s;
            return this;
        }

        public Builder locale(Locale locale) {
            this.locale = locale;
            return this;
        }

        public FileItem build() {
            return new FileItem(iconName, name, path, lastModified, size, description, locale);
        }
    }
}
