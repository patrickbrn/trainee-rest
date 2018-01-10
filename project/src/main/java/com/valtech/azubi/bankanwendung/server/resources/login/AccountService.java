package com.valtech.azubi.bankanwendung.server.resources.login;

import com.valtech.azubi.bankanwendung.model.dto.BenutzerDTO;
import com.valtech.azubi.bankanwendung.model.dto.LogInSession;
import com.valtech.azubi.bankanwendung.model.dto.UserAuthContainer;
import com.valtech.azubi.bankanwendung.server.resources.SessionManager;
import com.valtech.azubi.bankanwendung.server.services.UserService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Slf4j
@Path("/")
public class AccountService {
    private SessionManager sessionManager = SessionManager.getInstance();
    private UserService userService = UserService.getInstance();

    public AccountService() {
    }


    @POST
    @Path("/login")
    @Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public LogInSession authentication(@Context HttpServletRequest request, UserAuthContainer user) {
        System.out.println(user.toString());
        String ipAdress =request.getRemoteAddr();
        System.out.println(ipAdress);

        try {
            BenutzerDTO BenutzerDTO = new BenutzerDTO(userService.anmeldenBenutzer(user));
            BenutzerDTO.setRolle(user.getRolle());
            LogInSession session = new LogInSession(sessionManager.generateID(), BenutzerDTO, ipAdress);
            sessionManager.addSession(session);
            log.info("Log in erfolgreich: user {}", user.getUsername());
            System.out.println("----Session return---");
            return session;

        } catch (Exception e) {
            log.error("Anmeldeversuch für Benutzer \"" + user.getUsername() + "\" unterbunden " + e.getMessage(), e);
            throw new NotAuthorizedException(e.getMessage());
        }

    }

@DELETE
@Path("/logout")
public Response logOutSession(@HeaderParam("sessID") String sessID){

        sessionManager.removeSession(sessID);
        return Response.status(200).build();
}




}
