package com.valtech.azubi.bankanwendung.server.filter;

import com.valtech.azubi.bankanwendung.model.core.Rolle;
import com.valtech.azubi.bankanwendung.model.dto.BenutzerDTO;
import com.valtech.azubi.bankanwendung.server.resources.SessionManager;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Priority;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Method;

@Slf4j
@Provider
@RolesAuthorization
@Priority(Priorities.AUTHORIZATION)
public class Authorization implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    public Authorization() {

    }

    @Override
    public void filter(ContainerRequestContext requestContext) {
        System.out.println("****** AUTHORIZATION ******");
        String sessID = requestContext.getHeaderString("sessID");
        SessionManager sessionManager = SessionManager.getInstance();
        BenutzerDTO user = sessionManager.sucheBySess(sessID);

        try {
            SessionManager.getInstance().isSessionValid(sessID);
        } catch (Exception e) {
            log.error("Session invalid: ", e);
            throw new ForbiddenException(e.getMessage());
        }
        Method method = resourceInfo.getResourceMethod();
        Rolle[] roles = method.getAnnotation(RolesAuthorization.class).value();
        boolean isRoleValid=false;

        if (roles[0].equals(Rolle.ALL)) {
            isRoleValid=true;
        } else {
            for (Rolle r : roles) {
                if (r.equals(user.getRolle())) {
                    isRoleValid = true;
                    break;
                }
            }
        }

        if(!isRoleValid){
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            log.info("Authorization error for User {}", user.getBenutzerNr());
            System.out.println("******ROLE INSUFFICIENT*****");
        }

        requestContext.getHeaders().add("X-Role",user.getRolle().name());
    }

}
