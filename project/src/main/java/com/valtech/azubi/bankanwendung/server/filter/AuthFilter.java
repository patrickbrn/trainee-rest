package com.valtech.azubi.bankanwendung.server.filter;


import com.valtech.azubi.bankanwendung.model.dto.BenutzerDTO;
import com.valtech.azubi.bankanwendung.server.resources.SessionManager;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Priority;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

@Slf4j
@PreMatching
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthFilter implements ContainerRequestFilter {


    @Override
    public void filter(ContainerRequestContext requestContext) {
        System.out.println("--PRE Filter--");
        String sessionID=requestContext.getHeaderString("sessID");
        String benutzerNr = getBenutzerBySession(sessionID);

        try {
            SessionManager.getInstance().isSessionValid(sessionID);
        } catch (Exception e) {
            log.error("Session invalid user {}", benutzerNr);
            throw new ForbiddenException(e.getMessage());
        }

        if (benutzerNr == null) {
            System.out.println("Benutzer null");
            log.error("Benutzer (id) existiert nicht");
            ResponseBuilder responseBuilder = Response.serverError();
            Response response = responseBuilder.status(Status.FORBIDDEN).build();
            requestContext.abortWith(response);
        }
     //
        requestContext.getHeaders().add("X-User", benutzerNr);

    }


    public String getBenutzerBySession(String sessID) {

        if (sessID != null && !sessID.isEmpty()) {
            BenutzerDTO benutzer = SessionManager.getInstance().sucheBySess(sessID);
            System.out.println(benutzer);
            if (benutzer != null) {
                return String.valueOf(benutzer.getBenutzerNr());
            }
        }
        return null;
    }

    public boolean validateSession(String sessID) {

        return sessID != null && !sessID.isEmpty() && SessionManager.getInstance().sucheBySess(sessID) != null;
    }
}

