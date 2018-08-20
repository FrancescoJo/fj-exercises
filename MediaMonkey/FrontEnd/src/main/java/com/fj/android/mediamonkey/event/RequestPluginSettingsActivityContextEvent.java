/*
 * MediaMonkey Project
 * Licenced under Apache license 2.0. Read LICENSE for details.
 */
package com.fj.android.mediamonkey.event;

import com.fj.android.mediamonkey.util.event.Event;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 03 - Dec - 2016
 */
public class RequestPluginSettingsActivityContextEvent implements Event {
    public RequestPluginSettingsActivityContextEvent() {
    }

    @Override
    public String getName() {
        return RequestPluginSettingsActivityContextEvent.class.getSimpleName();
    }
}
