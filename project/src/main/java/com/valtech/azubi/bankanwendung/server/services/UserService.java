package com.valtech.azubi.bankanwendung.server.services;


import com.valtech.azubi.bankanwendung.model.core.AktionsTyp;
import com.valtech.azubi.bankanwendung.model.core.BenutzerNichtGefundenException;
import com.valtech.azubi.bankanwendung.model.core.InvalidCredentialsException;
import com.valtech.azubi.bankanwendung.model.core.Rolle;
import com.valtech.azubi.bankanwendung.model.dto.BenutzerDTO;
import com.valtech.azubi.bankanwendung.model.dto.KundeDTO;
import com.valtech.azubi.bankanwendung.model.dto.UserAuthContainer;
import com.valtech.azubi.bankanwendung.model.entity.Aktion;
import com.valtech.azubi.bankanwendung.model.entity.Angestellter;
import com.valtech.azubi.bankanwendung.model.entity.Benutzer;
import com.valtech.azubi.bankanwendung.model.entity.Kunde;
import com.valtech.azubi.bankanwendung.server.dao.UserDaoLayer;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import static com.valtech.azubi.bankanwendung.server.services.DBService.*;

@Slf4j
public class UserService {
    private static final UserService instance = new UserService();
    private UserDaoLayer kundeDaoL = UserDaoLayer.getInstance();

    public static UserService getInstance() {
        return instance;
    }

    public Benutzer anmeldenBenutzer(UserAuthContainer user) throws BenutzerNichtGefundenException, InvalidCredentialsException {

        openSession();
        if (user.getRolle().equals(Rolle.KUNDE)) {
            return anmeldenKunde(user.getUsername(), user.getHashPIN());
        }
        if (user.getRolle().equals(Rolle.ANGESTELLTER)) {
            return anmeldenAngestellter(user.getUsername(), user.getHashPIN());
        }
        throw new BenutzerNichtGefundenException(user.getUsername() + " nicht gefunden!");


    }

    public Kunde anmeldenKunde(String anmeldekennung, String code) throws BenutzerNichtGefundenException, InvalidCredentialsException {
        Kunde k;


        try {
            int kundennummer = Integer.parseInt(anmeldekennung);
            k = getKundeById(kundennummer);
        } catch (NumberFormatException ne) {
            k = getKundeByAnmeldename(anmeldekennung);
            System.out.println("Kunde anmeldename gesetzt " + k.getAnmeldename());
        }
        if (!k.checkCode(code)) throw new InvalidCredentialsException("PIN Ungültig");
        return k;

    }

    public void resetPIN(int id) throws BenutzerNichtGefundenException {
        sessionTransbegin();
        getKundeById(id).setCode(generateCode());
        sessionTransCommit();
    }

    public Angestellter anmeldenAngestellter(String personalkennung, String code) throws BenutzerNichtGefundenException {
        Angestellter a = null;

        try {
            a = getAngestellter(Integer.parseInt(personalkennung));
        } catch (NumberFormatException nf) {
            try {
                a = getAngestellterByAnmeldename(personalkennung);
            } catch (BenutzerNichtGefundenException be) {
                log.error(be.getMessage(), be);
            }
        }

        if (a == null) {
            throw new BenutzerNichtGefundenException("Benutzer nicht gefunden");
        }

        return a.checkCode(code) ? a : null;
    }

    public List getallKunden() {
        return kundeDaoL.getallKunden();
    }

    public List<Kunde> getKunden(String name, String vorname) {

        return kundeDaoL.getKunden(name, vorname);
    }

    public List<Kunde> getKunden(String name) {
        return kundeDaoL.getKunden(name);
    }

    public Kunde getKundeByAnmeldename(String aname) {
        return kundeDaoL.getKundeByAnmeldename(aname);
    }


    public Kunde getKundeById(int kundennr) throws BenutzerNichtGefundenException {
        return kundeDaoL.getKundeById(kundennr);
    }

    public Angestellter getAngestellter(int personalnummer) throws BenutzerNichtGefundenException {
        return kundeDaoL.getAngestellter(personalnummer);
    }

    public Angestellter getAngestellterByAnmeldename(String anmeldename) throws BenutzerNichtGefundenException {
        return kundeDaoL.getAngestellterByAnmeldename(anmeldename);
    }

    public void updateKunde(int nr, BenutzerDTO benutzerDTO) throws BenutzerNichtGefundenException {
        kundeDaoL.update(nr, benutzerDTO);
    }


    public void entferneKunde(int id) throws BenutzerNichtGefundenException {

        Kunde kunde = getKundeById(id);
        kundeDaoL.delete(kunde);
    }

    public void setAccountCredentials(int benutzerNr, String username, String pass, String oldpass) throws BenutzerNichtGefundenException, InvalidCredentialsException {

        Kunde k = getKundeById(benutzerNr);
        Kunde k2 = getKundeByAnmeldename(username);

        if (k2 != null) {
            if (k2.getBenutzerNr() != k.getBenutzerNr()) {
                throw new InvalidCredentialsException("Anmeldename schon vergeben");
            }
        }
        if (!k.checkCode(oldpass)) {
            throw new InvalidCredentialsException("PIN stimmt nicht überein");
        }

        sessionTransbegin();
        k.setAnmeldename(username);
        k.setCode(pass);
        sessionTransCommit();

    }

    public boolean isAnmeldenameVergeben(String anmeldename) {
        Kunde kunde = getKundeByAnmeldename(anmeldename);

        return kunde != null;
    }

    public void neueAktion(AktionsTyp aktionsTyp, String grund, int betroffener_kunde, String betroffenes_konto, int operator) throws BenutzerNichtGefundenException {

        Angestellter angestellter = getAngestellter(operator);

        Aktion aktion = new Aktion(aktionsTyp, grund, betroffener_kunde, betroffenes_konto, angestellter);

        DBService.sessionTransbegin();
        DBService.sessionSave(aktion);
        DBService.sessionTransCommit();
    }

    public void benutzerAnlegen(String name, String vorname, LocalDate gebdatum, String code, String rolle, Rolle
            benutzerTYP) {

        sessionTransbegin();
        if (benutzerTYP.equals(Rolle.KUNDE)) {
            Kunde k = new Kunde();
            k.setBenutzer(name, vorname, gebdatum, code);
            session.save(k);
            System.out.println("Kunde angelegt: Kundennummer: " + k.getBenutzerNr());
            log.info("Kunde angelegt: " + k.getBenutzerNr());
        }
        if (benutzerTYP.equals(Rolle.ANGESTELLTER)) {
            Angestellter angestellter = new Angestellter();
            angestellter.setBenutzer(name, vorname, gebdatum, code);
            angestellter.setRolle(rolle);
            session.save(angestellter);
        }
        sessionTransCommit();
    }

    public KundeDTO neuerKunde(KundeDTO kundeDTO) {
        Kunde kunde = new Kunde();
        kunde.setName(kundeDTO.getName());
        kunde.setVorname(kundeDTO.getVorname());
        kunde.setGebdatum(kundeDTO.getGebDatum());
        kunde.setCode(generateCode());
        return new KundeDTO(kundeDaoL.create(kunde));
    }


    public String generateCode() {
        Random rnd = new Random();
        int value;

        value = rnd.nextInt(99999 - 11111) + 1111;
        System.out.println("Temporärer PIN:\n" + value + "\n");
        return String.valueOf(value);

    }
}