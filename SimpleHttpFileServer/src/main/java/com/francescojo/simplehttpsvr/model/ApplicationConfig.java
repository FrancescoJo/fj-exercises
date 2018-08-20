/*
 * SimpleHttpServer project
 *
 * Read LICENSE file in project root for licence details.
 */
package com.francescojo.simplehttpsvr.model;

import com.francescojo.simplehttpsvr.util.GsonPostProcessAdapter;
import com.francescojo.simplehttpsvr.util.GsonWrapper;
import com.francescojo.simplehttpsvr.util.RegexHashMap;
import com.francescojo.simplehttpsvr.util.StringPatternUtils;

import java.util.List;

/**
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Oct - 2016
 */
// IntelliJ inspector false positive - fields are read and written by GSON
@SuppressWarnings({"unused", "MismatchedQueryAndUpdateOfCollection", "WeakerAccess"})
public class ApplicationConfig implements GsonPostProcessAdapter.PostProcessHook {
    private Server                 server;
    private List<String>           nameFilter;
    private List<DescriptionEntry> descriptions;
    private RegexHashMap<String>   descriptionMap;

    public Server getServer() {
        return server;
    }

    public List<String> getNameFilter() {
        return nameFilter;
    }

    public RegexHashMap<String> getDescriptionMap() {
        return descriptionMap;
    }

    @Override
    public void onPostProcessGson() {
        this.descriptionMap = new RegexHashMap<>();

        for (DescriptionEntry entry : descriptions) {
            descriptionMap.put(StringPatternUtils.wildcard2Regex(entry.getName()), entry.getRemark());
        }
    }

    public static class Server {
        private String name;
        private int    port;

        public String getName() {
            return name;
        }

        public int getPort() {
            return port;
        }
    }

    public static class DescriptionEntry {
        private String name;
        private String remark;

        public String getName() {
            return name;
        }

        public String getRemark() {
            return remark;
        }
    }

    @Override
    public String toString() {
        return GsonWrapper.getInstance().toJson(this);
    }
}
