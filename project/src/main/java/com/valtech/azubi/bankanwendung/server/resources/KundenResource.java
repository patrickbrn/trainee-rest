package com.valtech.azubi.bankanwendung.server.resources;

import com.valtech.azubi.bankanwendung.model.core.AktionsTyp;
import com.valtech.azubi.bankanwendung.model.core.BenutzerNichtGefundenException;
import com.valtech.azubi.bankanwendung.model.core.InvalidCredentialsException;
import com.valtech.azubi.bankanwendung.model.core.Rolle;
import com.valtech.azubi.bankanwendung.model.dto.BenutzerDTO;
import com.valtech.azubi.bankanwendung.model.dto.KontoDTO;
import com.valtech.azubi.bankanwendung.model.dto.KundeDTO;
import com.valtech.azubi.bankanwendung.model.entity.Kunde;
import com.valtech.azubi.bankanwendung.server.filter.RolesAuthorization;
import com.valtech.azubi.bankanwendung.server.services.KontoService;
import com.valtech.azubi.bankanwendung.server.services.UserService;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Path("/kunden")
public class KundenResource {
    private UserService userService = UserService.getInstance();

    @GET
    @Path("{Kunde}/")
    @RolesAuthorization({Rolle.ANGESTELLTER, Rolle.KUNDE})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public KundeDTO getKunde(@PathParam("Kunde") int kundenNr, @HeaderParam("X-User") int xkundenNr, @HeaderParam("X-Role") Rolle rolle) {
        Kunde kunde;

        try {
            if (rolle.equals(Rolle.ANGESTELLTER)) {
                kunde = userService.getKundeById(kundenNr);
            } else {
                kunde = userService.getKundeById(xkundenNr);
            }
            return new KundeDTO(kunde);
        } catch (BenutzerNichtGefundenException e) {
            log.error(e.getMessage(), e);
            throw new NotFoundException(e.getMessage());
        }
    }

    @DELETE
    @Path("{Kunde}/")
    @RolesAuthorization(Rolle.ANGESTELLTER)
    public Response deleteKundeByID(@PathParam("Kunde") int id, @HeaderParam("Grund") String grund, @HeaderParam("X-User") int operator) {

        try {
            Kunde k = userService.getKundeById(id);
            KontoService.getInstance().entferneKontenOfKunde(id);
            userService.entferneKunde(id);
            userService.neueAktion(AktionsTyp.KUNDE_GELÖSCHT, grund, id, k.getKonten().toString(), operator);
            log.info("Kunde erfolgreich gelöscht: " + id);

        } catch (BenutzerNichtGefundenException e) {
            log.error("Löschversuch nicht möglich da: " + e.getMessage(), e);
            throw new NotFoundException(e.getMessage());
        }
        return Response.status(Response.Status.ACCEPTED).entity("Kunde gelöscht").build();
    }

    @PUT
    @Path("/credentials")
    public Response setCredentials(@HeaderParam("X-User") int benutzerNr, @FormParam("username") String username, @FormParam("pass") String pass, @FormParam("oldpass") String oldpass) {
        try {
            userService.setAccountCredentials(benutzerNr, username, pass, oldpass);
        } catch (BenutzerNichtGefundenException e) {
            throw new NotFoundException(e.getMessage());
        } catch (InvalidCredentialsException e) {
            log.error(e.getMessage(), e);
            throw new NotAcceptableException(e.getMessage());
        }
        return Response.status(200).entity("Anmeldedaten wurden erfolgreich geändert").build();
    }

    @GET
    @RolesAuthorization(Rolle.ANGESTELLTER)
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<KundeDTO> getKundenL(@QueryParam("startAt") int index, @QueryParam("size") int size) {

        List<Kunde> kundenL = userService.getallKunden();
        List<KundeDTO> kundeDTOS = new ArrayList<>();

        if (index < 0) {
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).build());
        }

        for (int i = index; i < index + size; i++) {
            if (i > kundenL.size() - 1) {
                break;
            }
            kundeDTOS.add(new KundeDTO(kundenL.get(i)));
        }

        return kundeDTOS;
    }

    @Path("{kundennr}/konten")
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @RolesAuthorization(Rolle.ALL)
    public List<KontoDTO> getKonten(@HeaderParam("X-User") int xkundenNr, @PathParam("kundennr") int kundennr, @HeaderParam("X-Role") Rolle rolle) {
        System.out.println("Get KKonten");
        Kunde kunde;


        try {
            if (!rolle.equals(Rolle.ANGESTELLTER)) {
                kunde = userService.getKundeById(xkundenNr);
            } else {
                kunde = userService.getKundeById(kundennr);
            }
            return new KundeDTO(kunde).getKontenDTO();
        } catch (BenutzerNichtGefundenException e) {
            throw new NotFoundException(e.getMessage());
        }

    }

    @PUT
    @Path("{Kunde}/pinreset")
    @RolesAuthorization(Rolle.ANGESTELLTER)
    public Response resetPIN(@PathParam("Kunde") int nr) {
        try {
            userService.resetPIN(nr);
            //     administration.neueAktion(AktionsTyp.PIN_ZURÜCKGESETZT,"Vergessen",nr,"acc",1);
            log.info("Pin zurückgesetzt: " + nr);
            return Response.status(200).entity(("PIN zurückgesetzt")).build();
        } catch (BenutzerNichtGefundenException e) {
            throw new NotFoundException(e.getMessage());
        }
    }


    @PUT
    @Path("{Kunde}/bearbeiten")
    @RolesAuthorization(Rolle.ANGESTELLTER)
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateKunde(@PathParam("Kunde") int nr, BenutzerDTO benutzerDTO) {

        try {
            userService.updateKunde(nr, benutzerDTO);
            return Response.status(200).entity(("Benutzerdaten geändert")).build();
        } catch (BenutzerNichtGefundenException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Path("/anlegen")
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @RolesAuthorization(Rolle.ANGESTELLTER)
    public KundeDTO kundeAnlegen(KundeDTO kunde) {

        if (kunde == null) throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).build());

        return userService.neuerKunde(kunde);

    }


    @Path("/suche")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @RolesAuthorization(Rolle.ANGESTELLTER)
    public List<BenutzerDTO> sucheKunde(@QueryParam("vorname") String vorname, @QueryParam("name") String name) {

        List<Kunde> kundenL = userService.getKunden(name, vorname);
        List<BenutzerDTO> benutzerL = new ArrayList<>();

        for (Kunde k : kundenL) {
            benutzerL.add(new BenutzerDTO(k));
        }

        return benutzerL;

    }
}
