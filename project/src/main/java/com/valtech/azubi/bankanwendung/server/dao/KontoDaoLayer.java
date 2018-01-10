package com.valtech.azubi.bankanwendung.server.dao;

import com.valtech.azubi.bankanwendung.model.core.*;
import com.valtech.azubi.bankanwendung.model.entity.*;
import com.valtech.azubi.bankanwendung.server.services.DBService;
import com.valtech.azubi.bankanwendung.server.services.UserService;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Query;
import java.util.Date;
import java.util.List;

import static com.valtech.azubi.bankanwendung.model.core.Kontomodell.GIROKONTO;
import static com.valtech.azubi.bankanwendung.model.core.Kontomodell.SPARKONTO;
import static com.valtech.azubi.bankanwendung.server.services.DBService.*;

@Slf4j
public class KontoDaoLayer {
    public static KontoDaoLayer instance = new KontoDaoLayer();
    public UserService userService = UserService.getInstance();

    public static KontoDaoLayer getInstance() {
        return instance;
    }


    public Konto createKonto(int kunden_id, Kontomodell modell) throws BenutzerNichtGefundenException {

        Kunde kunde = userService.getKundeById(kunden_id);

        sessionTransbegin();

        if (modell.equals(GIROKONTO)) {
            Girokonto gk = new Girokonto();
            gk.setKunde(kunde);
            gk.setDispo(200);
            session.save(gk.getProtokoll());
            session.save(gk);
            System.out.println("\nNeues " + gk.getClass().getName() + " angelegt für benutzer: " + kunde.getBenutzerNr() + " " + kunde + " Kontonummer: " + gk.getKontoNr());
            sessionTransCommit();
            return gk;
        }
        if (modell.equals(SPARKONTO)) {
            Sparkonto sk = new Sparkonto();
            sk.setKunde(kunde);
            session.save(sk.getProtokoll());
            session.save(sk);
            System.out.println("Neues " + sk.getClass().getName() + " angelegt für benutzer: " + kunde.getBenutzerNr() + "  Kontonummer: " + sk.getKontoNr());
            sessionTransCommit();
            return sk;
        }
        return null;

    }

    public void sperreKontoById(int nr) throws KontoNichtGefundenException {
        sessionTransbegin();
        getKontoByID(nr).setGesperrt(true);
        sessionTransCommit();
    }

    public void entsperreKontoById(int nr) throws KontoNichtGefundenException {
        sessionTransbegin();
        getKontoByID(nr).setGesperrt(false);
        sessionTransCommit();
    }

    public Konto getKontoByID(int kontonummer) throws KontoNichtGefundenException {
        Konto konto;
        Query query = session.createQuery("from Konto k where k.KontoNr=" + kontonummer);
        try {
            konto = (Konto) query.getResultList().get(0);
            log.info("Konto gefunden " + kontonummer);
        } catch (IndexOutOfBoundsException ie) {
            log.error(ie.getMessage());
            throw new KontoNichtGefundenException("Konto " + kontonummer + " existiert nicht", ie);
        }

        return konto;
    }

    public Konto getKontoOfKundeById(int kunden_id, int konto_id) throws KontoNichtGefundenException, KontoGesperrtException, BenutzerNichtGefundenException {

        Konto k = UserService.getInstance().getKundeById(kunden_id).sucheKonto(konto_id);

        if (k == null)
            throw new KontoNichtGefundenException("Kunde " + kunden_id + " besitzt kein Konto mit der Kontonummer " + konto_id);
        if (k.isGesperrt()) throw new KontoGesperrtException(k);

        return k;


    }

    // @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void einzahlenById(int konto_id, double betrag) throws NegativerBetragException, KontoGesperrtException, KontoNichtGefundenException {
        Konto konto = getKontoByID(konto_id);

        if (konto.isGesperrt()) {
            throw new KontoGesperrtException(konto);
        }
        sessionTransbegin();
        konto.addBetrag(betrag);
        sessionSave(konto.getProtokoll().addEintrag(null, konto, TransaktionsTyp.EINZAHLUNG, "Einzahlen", betrag));
        sessionTransCommit();
    }

    public void auszahlenById(int konto_id, double betrag) throws NegativerBetragException, AuszahlenException, KontoGesperrtException, KontoNichtGefundenException {
        Konto konto = getKontoByID(konto_id);

        if (konto.isGesperrt()) {
            throw new KontoGesperrtException(konto);
        }

        if (isangemeldetanGiro(konto)) {
            sessionTransbegin();
            Girokonto anganGiro = (Girokonto) konto;
            anganGiro.subBetrag(betrag);
            sessionSave(anganGiro.getProtokoll().addEintrag(anganGiro, null, TransaktionsTyp.AUSZAHLUNG, "Auszahlen", betrag));
            sessionTransCommit();
        } else {
            sessionTransbegin();
            konto.subBetrag(betrag);
            sessionSave(konto.getProtokoll().addEintrag(konto, null, TransaktionsTyp.AUSZAHLUNG, "Auszahlen", betrag));
            sessionTransCommit();
        }

    }

    public void neueUeberweisung(int von_kontoNr, int an_KontoNr, String vzweck, double betrag) throws KontoNichtGefundenException, KontoGesperrtException, NegativerBetragException {

        Konto vonKonto = getKontoByID(von_kontoNr);
        Konto anKonto = getKontoByID(an_KontoNr);


        if (betrag < 0) {
            throw new NegativerBetragException();
        }

        if (anKonto.isGesperrt()) throw new KontoGesperrtException(anKonto);

        sessionTransbegin();
        Transaktion t = new Transaktion(vonKonto, anKonto, TransaktionsTyp.UEBERWEISUNG, vzweck, betrag, new Date());
        session.save(t);
        sessionTransCommit();

    }

    public boolean isangemeldetanGiro(Konto konto) {

        return konto.getClass().getSimpleName().equals(Konto.GIROKONTO);
    }

    public void entferneKontenOfKunde(int id) throws BenutzerNichtGefundenException {

        List<Konto> kontoList = userService.getKundeById(id).getKonten();

        for (Konto k : kontoList) {
            entferneKonto(k);
        }
    }

    public void entferneKonto(Konto k) {
        sessionTransbegin();
        DBService.session.delete(k);
        sessionTransCommit();
    }

    public void entferneKontoById(int id) throws KontoNichtGefundenException {
        Konto konto = getKontoByID(id);
        sessionTransbegin();
        DBService.session.delete(konto);
        sessionTransCommit();
    }
}
