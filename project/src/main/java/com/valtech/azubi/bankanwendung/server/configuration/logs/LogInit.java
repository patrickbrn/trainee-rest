package com.valtech.azubi.bankanwendung.server.configuration.logs;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;

public class LogInit {
    static Logger logger = Logger.getLogger(LogInit.class);


    public static void main(String[] args) {
        String log4jCfgFile = System.getProperty("user.dir") + File.separator + "/log4j.properties";
        PropertyConfigurator.configure(log4jCfgFile);
        logger.info("First Test");
    }

}