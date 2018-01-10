package com.valtech.azubi.bankanwendung.server.resources;

import com.valtech.azubi.bankanwendung.model.dto.BenutzerDTO;
import com.valtech.azubi.bankanwendung.model.dto.LogInSession;

import java.util.*;

public class SessionManager {
    public static final SessionManager instance = new SessionManager();
    private List<LogInSession> logInSessionList = new ArrayList<>();

    public static SessionManager getInstance() {
        return instance;
    }

    public BenutzerDTO sucheBySess(String sessionID) {
        System.out.println("Session LIST size:" + logInSessionList.size());
        for (LogInSession session : logInSessionList) {
            if (session.getSessionID().equals(sessionID)) {
                System.out.println("Aktive Session gefunden: " + session.getSessionID());
                return session.getVonBenutzer();
            }
        }
        return null;
    }

    public LogInSession getSession(String sessionID) {

        for (LogInSession session : logInSessionList) {
            if (session.getSessionID().equals(sessionID)) {
                return session;
            }
        }
        return null;
    }

    public boolean isSessionValid(String sessID) throws Exception {

        LogInSession session = getSession(sessID);
        if (session == null) throw new Exception("Session not found");

        if (new Date().after(session.getEndDate())) throw new Exception("Sitzung ist abgelaufen");

        return true;
    }

    public void removeSession(String sessID) {

        Iterator<LogInSession> it = logInSessionList.iterator();

        while (it.hasNext()) {
            LogInSession s = it.next();

            if (s.getSessionID().equals(sessID)) {
                logInSessionList.remove(s);
                System.out.println("--LOGGED OUT--");
                break;
            }
        }
    }


    public void addSession(LogInSession session) {
        logInSessionList.add(session);
    }

    public List<LogInSession> getLogInSessionList() {
        return logInSessionList;
    }

    public void setLogInSessionList(List<LogInSession> logInSessionList) {
        this.logInSessionList = logInSessionList;
    }

    public String generateID() { //UUID generieren

        String sessID = String.valueOf(UUID.randomUUID());
        System.out.println("SessionID generiert: " + sessID);

        return sessID;
    }

}