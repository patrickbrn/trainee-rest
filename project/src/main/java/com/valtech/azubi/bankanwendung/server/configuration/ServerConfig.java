package com.valtech.azubi.bankanwendung.server.configuration;

import com.valtech.azubi.bankanwendung.server.services.DBService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ServerConfig implements ServletContextListener{


    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        DBService.openSession();
    }
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        DBService.closeSession();
    }
}
