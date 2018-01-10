package com.valtech.azubi.bankanwendung.server.dao;

import com.valtech.azubi.bankanwendung.model.core.BenutzerNichtGefundenException;
import com.valtech.azubi.bankanwendung.model.dto.BenutzerDTO;
import com.valtech.azubi.bankanwendung.model.entity.Angestellter;
import com.valtech.azubi.bankanwendung.model.entity.Kunde;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Query;
import java.util.List;

import static com.valtech.azubi.bankanwendung.server.services.DBService.*;

@Slf4j
public class UserDaoLayer {
    private static UserDaoLayer instance = new UserDaoLayer();

    public static UserDaoLayer getInstance() {
        return instance;
    }

    public Kunde create(Kunde k) {
        sessionTransbegin();
        sessionSave(k);
        sessionTransCommit();
        return k;
    }

    public void delete(Kunde k) {
        sessionTransbegin();
        session.delete(k);
        sessionTransCommit();
    }

    public List getallKunden() {
        return session.createQuery("from Kunde").getResultList();
    }

    public List<Kunde> getKunden(String name, String vorname) {

        Query query = session.createNamedQuery(Kunde.GET_KUNDEN_BY_NAME).setParameter("name", name + "%").setParameter("vorname", vorname + "%");
        return (List<Kunde>) query.getResultList();
    }

    public List<Kunde> getKunden(String name) {
        Query query = session.createNamedQuery("getKundeBySurName").setParameter("name", name);
        return (List<Kunde>) query.getResultList();
    }

    public Kunde getKundeByAnmeldename(String aname) {
        Query query = session.createNamedQuery(Kunde.GET_KUNDEN_BY_ANMELDENAME).setParameter("anmeldename", aname);
        try {
            Kunde k = (Kunde) query.getResultList().get(0);
            return !k.getAnmeldename().equals("") ? k : null;
        } catch (IndexOutOfBoundsException ie) {
            log.error(ie.getMessage(), ie);
        }
        return null;
    }

    public List<Kunde> getKundenByVorname(String vorname) {
        Query query = session.createNamedQuery("getKundeByFirstName").setParameter("vorname", vorname);
        return (List<Kunde>) query.getResultList();
    }


    public Kunde getKundeById(int kundennr) throws BenutzerNichtGefundenException {
        Query query = session.createNamedQuery(Kunde.GET_KUNDEN_BY_KUNDENNUMMER).setParameter("benutzerNr", kundennr);
        Kunde k;
        try {
            k = (Kunde) query.getResultList().get(0);
        } catch (IndexOutOfBoundsException ie) {
            log.error(ie.getMessage(), ie);
            throw new BenutzerNichtGefundenException("Kunde mit der Benutzernummer " + kundennr + " nicht gefunden");
        }
        return k;
    }

    public Angestellter getAngestellter(int personalnummer) throws BenutzerNichtGefundenException {
        Query query = session.createNamedQuery("getPersonByID").setParameter("nr", personalnummer);
        try {
            return (Angestellter) query.getResultList().get(0);
        } catch (IndexOutOfBoundsException ie) {
            log.error(ie.getMessage(), ie);
            throw new BenutzerNichtGefundenException("Angestellter mit der Nummer " + personalnummer + " nicht gefunden");
        }

    }

    public Angestellter getAngestellterByAnmeldename(String anmeldename) throws BenutzerNichtGefundenException {
        Query query = session.createNamedQuery(Angestellter.GET_ANGESTELLTER_BY_ANMELDENAMEN).setParameter("anmeldename", anmeldename);

        try {
            Angestellter angestellter = (Angestellter) query.getResultList().get(0);
            if (angestellter.getAnmeldename().length() > 0) {
                return angestellter;
            }
        } catch (IndexOutOfBoundsException ie) {
            log.error(ie.getMessage(), ie);
            throw new BenutzerNichtGefundenException("Benutzer " + anmeldename + " nicht gefunden");
        }
        return null;
    }

    public void update(int id, BenutzerDTO benutzerDTO) throws BenutzerNichtGefundenException {
        Kunde kunde = getKundeById(id);
        kunde.setAnmeldename(benutzerDTO.getAnmeldename());
        kunde.setGebdatum(benutzerDTO.getGebDatum());
        kunde.setVorname(benutzerDTO.getVorname());
        kunde.setName(benutzerDTO.getName());
        sessionTransbegin();
        sessionSave(kunde);
        sessionTransCommit();
    }
}
