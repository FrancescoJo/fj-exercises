/*
 * SimpleHttpServer project
 *
 * Read LICENSE file in project root for licence details.
 */
package com.francescojo.simplehttpsvr;

import com.francescojo.simplehttpsvr.model.ApplicationConfig;
import com.francescojo.simplehttpsvr.util.GsonPostProcessAdapter;
import com.francescojo.simplehttpsvr.util.GsonWrapper;
import com.google.common.io.ByteStreams;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.*;
import java.net.URL;

/**
 * This class is very difficult to unit test, because it does file I/O.
 *
 * @author Francesco Jo(nimbusob@gmail.com)
 * @since 19 - Oct - 2016
 */
/*default*/ class ApplicationConfigHelper {
    private static final Logger LOG                 = LoggerFactory.getLogger(ApplicationConfigHelper.class);
    private static final String DEFAULT_CONFIG_FILE = "simplehttpsvr-config.json";

    /*default*/ ApplicationConfig loadConfig(@NotNull File configFile) {
        ApplicationConfig config = loadConfigInternal(configFile);
        if (null != config) {
            LOG.info("Loaded configuration file {}", configFile.getAbsolutePath());
        } else {
            try {
                File configFile2 = copyDefaultConfig();
                config = loadConfig(configFile2);
            } catch (IOException e) {
                LOG.error("Cannot start application. ({})", e.getMessage());
                return null;
            }
        }

        return config;
    }

    private ApplicationConfig loadConfigInternal(File file) {
        GsonWrapper gson = GsonWrapper.getInstanceWith(new GsonBuilder().registerTypeAdapterFactory(new
                GsonPostProcessAdapter()));
        try (FileReader fr = new FileReader(file)) {
            JsonElement jsonElement = new JsonParser().parse(fr);
            return gson.fromJson(jsonElement, ApplicationConfig.class);
        } catch (IOException e) {
            return null;
        }
    }

    private File copyDefaultConfig() throws IOException {
        URL uri = Application.class.getClassLoader().getResource(DEFAULT_CONFIG_FILE);
        if (null == uri) {
            throw new IOException("This executable binary is damaged.");
        }

        File configFile = new File(DEFAULT_CONFIG_FILE);
        try (InputStream is = uri.openStream(); FileOutputStream os = new FileOutputStream(configFile)) {
            ByteStreams.copy(is, os);
        }

        LOG.info("Copied default config file to:  {}", configFile.getAbsolutePath());
        return configFile;
    }
}
