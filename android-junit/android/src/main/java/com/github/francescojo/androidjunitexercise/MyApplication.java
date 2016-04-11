/*
 * @(#)MyApplication.java $Version 13 - Nov - 2015
 *
 * Licenced under Apache License v2.0.
 * Read http://www.apache.org/licenses/ for details.
 */
package com.github.francescojo.androidjunitexercise;

import android.app.Application;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 */
public class MyApplication extends Application {
    /**
     * Marks this class as singleton instance and we can reduce harmful <code>android.app.Activity</code>
     * references in many logic which needs just an <code>ApplicationContext</code> rather than <code>ActivityContext</code>.
     */
    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.instance = this;
    }

    public static MyApplication getInstance() {
        return instance;
    }
}
