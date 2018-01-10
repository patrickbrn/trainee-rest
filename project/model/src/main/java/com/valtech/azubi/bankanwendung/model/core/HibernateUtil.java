package com.valtech.azubi.bankanwendung.model.core;

import com.valtech.azubi.bankanwendung.model.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.SessionFactoryObserver;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {

    private static final String DEFAULT_CONFIG_PATH = "core/hibernate.cfg.xml";

    public static Session openSession(String configFilePath) {
        SessionFactory sessionFactory;

        try {
            Configuration config = getConfiguration(configFilePath);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();

            config.setSessionFactoryObserver(new SessionFactoryObserver() {
                private static final long  serialVersionUID = 1L;

                @Override
                public void sessionFactoryCreated(SessionFactory factory) {}

                @Override
                public void sessionFactoryClosed(SessionFactory factory) {}
            });
            sessionFactory = config.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }

        return sessionFactory.openSession();
    }

    public static Session openSession() {
        return openSession(DEFAULT_CONFIG_PATH);
    }

    private static  Configuration getConfiguration(String configFilePath) {
        Configuration cfg = new Configuration().configure(configFilePath);



        cfg.addAnnotatedClass(Konto.class);
        cfg.addAnnotatedClass(Girokonto.class );
        cfg.addAnnotatedClass(Sparkonto.class);
        cfg.addAnnotatedClass(Eintrag.class);
        cfg.addAnnotatedClass(Protokoll.class);
        cfg.addAnnotatedClass(Benutzer.class);
        cfg.addAnnotatedClass(Kunde.class);
        cfg.addAnnotatedClass(Angestellter.class);
        cfg.addAnnotatedClass(Transaktion.class);
        cfg.addAnnotatedClass(Aktion.class);
       return cfg;
    }
}

