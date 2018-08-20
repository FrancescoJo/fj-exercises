/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.plugin;

import com.mediamonkey.android.lib.dto.VersionDto;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 27 - Nov - 2016
 */
public class PluginException extends Exception {
    private VersionDto oldVersion;

//    public PluginException() {
//        super();
//    }

    public PluginException(String message) {
        super(message);
    }

    public void setOldVersion(VersionDto version) {
        this.oldVersion = version;
    }

    public VersionDto getOldVersion() {
        return oldVersion;
    }

//    public PluginException(Throwable cause) {
//        super(cause);
//    }
}
