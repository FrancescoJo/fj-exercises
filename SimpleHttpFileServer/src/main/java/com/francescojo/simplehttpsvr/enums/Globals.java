/*
 * SimpleHttpServer project
 *
 * Read LICENSE file in project root for licence details.
 */
package com.francescojo.simplehttpsvr.enums;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 20 - 10월 - 2016
 */
public enum Globals {
    SERVER_NAME("_SERVER_NAME"),
    SERVER_DATE("_SERVER_DATE"),
    SERVER_TIMEZONE("_SERVER_TIMEZONE"),
    REQUEST_IP("_REQUEST_IP"),
    EXECUTION_TIME("_EXECUTION_TIME"),
    PATH_STATIC("_PATH_STATIC"),
    PATH_STATIC_ICONS("_PATH_STATIC_ICO");

    public final String tag;

    Globals(String tag) {
        this.tag = tag;
    }
}
