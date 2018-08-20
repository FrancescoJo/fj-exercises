/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.mediamonkey.android.plugin;

import android.support.annotation.Nullable;

import com.mediamonkey.android.lib.util.DataStorable;

import java.util.List;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 10 - Dec - 2016
 */
public interface DataStoreInterface extends ShutdownableService {
    int getDataStoreVersion();

    @Nullable
    List<Class<? extends DataStorable>> getStorables();
}
