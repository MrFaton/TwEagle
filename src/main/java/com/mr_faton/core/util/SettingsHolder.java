package com.mr_faton.core.util;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 15.09.2015
 */
public class SettingsHolder {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.util.SettingsHolder");
    private static final ClassLoader LOADER = SettingsHolder.class.getClassLoader();
    private static final Properties SETTINGS = new Properties();
    private static final String FILE_NAME = "Settings.properties";

    public static void loadSettings() throws IOException {
        InputStream settingsInputStream = LOADER.getResourceAsStream(FILE_NAME);
        SETTINGS.load(settingsInputStream);
        logger.debug("settings file loaded, it's " + SETTINGS.toString());
    }

    public static String getSetupByKey(String key) {
        logger.debug("requested key=" + key + ", found value=" + SETTINGS.getProperty(key));
        return SETTINGS.getProperty(key);
    }
}
