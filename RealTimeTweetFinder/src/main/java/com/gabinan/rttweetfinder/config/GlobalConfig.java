package com.gabinan.rttweetfinder.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * A config object to get the properties from the config file
 * 
 * @author Gabriel Nan
 */
@Log4j
public class GlobalConfig {

    private static final Properties config = new Properties();
    private static final String CONFIG_PATH = "/home/gabriel/NetBeansProjects/RTTweetFinder/config.txt";

    @Autowired
    public GlobalConfig() {

        try {
            config.load(new FileInputStream(CONFIG_PATH));
        } catch (IOException ex) {
            myLogger.error("Could not load config from " + CONFIG_PATH + " \n Check if file exists & has all required fields!");
            myLogger.error(ex.getMessage());

        }

    }

    public String get(String propertyName) {
        return config.getProperty(propertyName);
    }

    public int getInt(String propertyName) {

        return Integer.parseInt(config.getProperty(propertyName));
    }
}
