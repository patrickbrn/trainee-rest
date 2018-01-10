package com.valtech.azubi.bankanwendung.server.services;

import com.valtech.azubi.bankanwendung.model.core.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DBService {
public static Session session;
    public static final DBService instance = new DBService();

    public static DBService getInstance() {
        return instance;
    }

    public static Session getSession() {
        return session;
    }

    public static Session openSession() {


        session = HibernateUtil.openSession();
        System.out.println("\n-------NEW SESSION STARTED-----");
        return session;
    }

    public static void closeSession() {

        if (session.isOpen()) {
            session.close();
            System.out.println("\n-------SESSION CLOSED------");
        }
    }



    public static Transaction sessionTransbegin() {
        if (!session.getTransaction().isActive()) {
            System.out.println("\n-------Begin Transaction------");
            return session.beginTransaction();
        }
        return null;
    }

    public static void sessionTransCommit() {
        if (session.getTransaction().isActive()) {
            session.getTransaction().commit();
            System.out.println("\n----------COMMITED--------");
        }
    }

    public static void sessionSave(Object o){
        session.save(o);
    }
}