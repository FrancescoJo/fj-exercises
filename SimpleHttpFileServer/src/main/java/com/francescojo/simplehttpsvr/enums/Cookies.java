/*
 * SimpleHttpServer project
 *
 * Read LICENSE file in project root for licence details.
 */
package com.francescojo.simplehttpsvr.enums;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 21 - Oct - 2016
 */
public enum Cookies {
    PREFERRED_LANGUAGE("prefLang", String.class);

    public final String   name;
    public final Class<?> type;

    Cookies(String name, Class<?> type) {
        this.name = name;
        this.type = type;
    }
}
