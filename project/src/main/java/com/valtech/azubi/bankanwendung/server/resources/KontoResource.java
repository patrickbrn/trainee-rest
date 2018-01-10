package com.valtech.azubi.bankanwendung.server.resources;

import com.valtech.azubi.bankanwendung.model.core.*;
import com.valtech.azubi.bankanwendung.model.dto.KontoDTO;
import com.valtech.azubi.bankanwendung.model.entity.Eintrag;
import com.valtech.azubi.bankanwendung.model.entity.Konto;
import com.valtech.azubi.bankanwendung.server.filter.RolesAuthorization;
import com.valtech.azubi.bankanwendung.server.services.KontoService;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.List;

@Slf4j
@Path("/konten")
public class KontoResource {
    private KontoService kontoService = KontoService.getInstance();

    @Path("/anlegen/{kundennr}")
    @PUT
    @RolesAuthorization(Rolle.ANGESTELLTER)
    public Response erstelleKonto(@PathParam("kundennr") int kundenNr, @QueryParam("kontomodell") Kontomodell kontomodell) {
        System.out.println("Kontomodell: " + kontomodell);
        try {
            if (kontoService.createKonto(kundenNr, kontomodell) != null) {
                String msg = " Ein " + kontomodell + " für den Kunden " + kundenNr + " wurde erfolgreich angelegt";
                log.info(msg);
                return Response.status(Response.Status.CREATED).entity(msg).build();

            }

            return Response.status(500).entity("FEHLGESCHLAGEN: Konto konnte nicht angelegt werden!").build();
        } catch (BenutzerNichtGefundenException e) {
            log.error(e.getMessage(), e);
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }


    @Path("/{KontoNr}/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAuthorization(Rolle.ALL)
    public KontoDTO getKonto(@HeaderParam("X-User") int kundenNr, @PathParam("KontoNr") int kontonr, @HeaderParam("X-Role") Rolle rolle) {
        System.out.println("X-Kundennummer: " + kundenNr);
        System.out.println("KontoNr: " + kontonr);

        try {
            KontoDTO kontoDTO;
            if (rolle.equals(Rolle.ANGESTELLTER)) {
                kontoDTO = new KontoDTO(kontoService.getKontoByID(kontonr));
            } else {
                kontoDTO = new KontoDTO(kontoService.getKontoOfKundeById(kundenNr, kontonr));
            }
            System.out.println(kontoDTO);
            return kontoDTO;
        } catch (KontoNichtGefundenException e) {
            log.error(e.getMessage(),e);
            throw new NotFoundException(e.getMessage());
        } catch (KontoGesperrtException e) {
            throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build());
        } catch (BenutzerNichtGefundenException e) {
            log.error(e.getMessage(),e);
            throw new BadRequestException(e.getMessage());
        }
    }


    @Path("/{kontonr}/einzahlen")
    @PUT
    @RolesAuthorization(Rolle.KUNDE)
    public Response einzahlen(@PathParam("kontonr") int kontonr, @HeaderParam("X-User") int kundenNr, @QueryParam("betrag") double betrag) {
        System.out.println("kontoNR: " + kontonr);
        System.out.println("Kundennr: " + kundenNr);
        System.out.println("betrag: " + betrag);

        try {
            kontoService.einzahlenById(kundenNr, kontonr, betrag);
            return Response.status(200).entity((betrag + "€ ERFOLGREICH eingezahlt")).build();
        } catch (NegativerBetragException e) {
            log.error(e.getMessage(), e);
            throw new BadRequestException(e.getMessage());
        } catch (KontoGesperrtException e) {
            throw new ForbiddenException(e.getMessage());
        } catch (KontoNichtGefundenException e) {
            throw new NotFoundException(e.getMessage());
        } catch (BenutzerNichtGefundenException e) {
            throw new BadRequestException(e.getMessage());

        }

    }

    @Path("/{kontonr}/auszahlen")
    @PUT
    @RolesAuthorization(Rolle.KUNDE)
    public Response auszahlen(@PathParam("kontonr") int kontonr, @QueryParam("betrag") double betrag, @HeaderParam("X-User") int kundenNr) {

        try {
            kontoService.auszahlenById(kundenNr, kontonr, betrag);
            return Response.status(200).entity((betrag + "€ ERFOLGREICH ausgezahlt")).build();
        } catch (NegativerBetragException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (KontoGesperrtException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        } catch (KontoNichtGefundenException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (AuszahlenException e) {
            return Response.status(500).entity(e.getMessage()).build();
        } catch (BenutzerNichtGefundenException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ForbiddenException(e.getMessage());
        }
    }

    @Path("/{kontoNr}/transaktion")
    @PUT
    @RolesAuthorization(Rolle.KUNDE)
    public Response neueUeberweisung(@HeaderParam("X-User") int kundenNr, @PathParam("kontoNr") int kontoNr,
                                     @FormParam("zielKonto") int zielKonto, @FormParam("vzweck") String vzweck, @FormParam("betrag") double betrag) {

        try {
            kontoService.neueUeberweisung(kundenNr, kontoNr, zielKonto, vzweck, betrag);
            return Response.status(Response.Status.OK).entity(("Transaktionsauftrag erfolgreich")).build();
        } catch (KontoGesperrtException e) {
            return Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
        } catch (KontoNichtGefundenException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (NegativerBetragException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (BenutzerNichtGefundenException e) {
            throw new BadRequestException(e.getMessage());
        }

    }


    @Path("/{kontonr}/sperren")
    @PUT
    @RolesAuthorization(Rolle.ANGESTELLTER)
    public Response sperren(@PathParam("kontonr") int kontoNr) {

        try {
            kontoService.sperreKontoById(kontoNr);
            return Response.status(200).entity(("Konto wurde erfolgreich gesperrt")).build();
        } catch (KontoNichtGefundenException e) {
            return Response.status(500).entity(e.getMessage()).build();
        }
    }

    @Path("/{kontonr}/entsperren")
    @PUT
    @RolesAuthorization(Rolle.ANGESTELLTER)
    public Response entsperren(@PathParam("kontonr") int kontoNr) {

        try {
            kontoService.entsperreKontoById(kontoNr);
            return Response.status(200).entity(("Konto wurde erfolgreich entsperrt")).build();
        } catch (KontoNichtGefundenException e) {
            return Response.status(500).entity(e.getMessage()).build();
        }
    }


    @GET
    @Path("{nr}/protokoll/download")
    @Produces("application/pdf")
    @RolesAuthorization({Rolle.ANGESTELLTER, Rolle.KUNDE})
    public Response pdfProtokoll(@PathParam("nr") int kontoNr, @HeaderParam("X-User") int kundenNr, @HeaderParam("X-Role") Rolle rolle) {
        Konto konto;

        try {
            if (!rolle.equals(Rolle.ANGESTELLTER)) {
                konto = kontoService.getKontoOfKundeById(kundenNr, kontoNr);
            } else {
                konto = kontoService.getKontoByID(kontoNr);
            }
            PDFAuszug pdfAuszug = new PDFAuszug(konto);
            File file = new File(pdfAuszug.getDEST());

            if (file.exists()) {
                return Response.ok(file).header("Content-Disposition", "attachment; filename=" + file.getName()).build();
            }


        } catch (KontoNichtGefundenException e) {
            throw new NotFoundException(e.getMessage());
        } catch (BenutzerNichtGefundenException e) {
            throw new BadRequestException(e.getMessage());
        } catch (KontoGesperrtException e) {
            throw new ForbiddenException(e.getMessage());
        }
        return Response.status(500).build();

    }

    @GET
    @Path("{KontoNr}/protokoll")
    @RolesAuthorization({Rolle.ANGESTELLTER, Rolle.KUNDE})
    public List<Eintrag> getProtokoll(@PathParam("KontoNr") int kontoNr, @HeaderParam("X-User") int kundenNr, @HeaderParam("X-Role") Rolle rolle) {
        Konto konto;

        try {
            if (rolle.equals(Rolle.ANGESTELLTER)) {
                konto = kontoService.getKontoByID(kontoNr);
            } else {
                konto = kontoService.getKontoOfKundeById(kundenNr, kontoNr);
            }
            return konto.getProtokoll().getEintraege();
        } catch (KontoGesperrtException e) {
            throw new ForbiddenException(e.getMessage());
        } catch (BenutzerNichtGefundenException e) {
            throw new BadRequestException(e.getMessage());
        } catch (KontoNichtGefundenException e) {
            throw new NotFoundException(e.getMessage());
        }

    }
}
