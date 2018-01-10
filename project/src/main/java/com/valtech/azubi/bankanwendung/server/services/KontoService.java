package com.valtech.azubi.bankanwendung.server.services;


import com.valtech.azubi.bankanwendung.model.core.*;
import com.valtech.azubi.bankanwendung.model.entity.Konto;
import com.valtech.azubi.bankanwendung.server.dao.KontoDaoLayer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KontoService {
    private static final KontoService instance = new KontoService();
    private KontoDaoLayer kontoDaoLayer = KontoDaoLayer.getInstance();


    public static KontoService getInstance() {
        return instance;
    }

    public Konto createKonto(int kunden_id, Kontomodell kontomodell) throws BenutzerNichtGefundenException {
        return kontoDaoLayer.createKonto(kunden_id, kontomodell);
    }

    public void sperreKontoById(int nr) throws KontoNichtGefundenException {
        kontoDaoLayer.sperreKontoById(nr);

    }

    public void entsperreKontoById(int nr) throws KontoNichtGefundenException {
        kontoDaoLayer.entsperreKontoById(nr);
    }

    public Konto getKontoByID(int kontonummer) throws KontoNichtGefundenException {

        return kontoDaoLayer.getKontoByID(kontonummer);
    }

    public Konto getKontoOfKundeById(int kunden_id, int kontonummer) throws KontoGesperrtException, KontoNichtGefundenException, BenutzerNichtGefundenException {

        return kontoDaoLayer.getKontoOfKundeById(kunden_id, kontonummer);


    }

    public void einzahlenById(int kunden_id, int konto_id, double betrag) throws NegativerBetragException, KontoGesperrtException, KontoNichtGefundenException, BenutzerNichtGefundenException {
        getKontoOfKundeById(kunden_id, konto_id);
        kontoDaoLayer.einzahlenById(konto_id, betrag);
    }

    public void auszahlenById(int kunden_id, int konto_id, double betrag) throws NegativerBetragException, AuszahlenException, KontoGesperrtException, KontoNichtGefundenException, BenutzerNichtGefundenException {
        getKontoOfKundeById(kunden_id, konto_id);
        kontoDaoLayer.auszahlenById(konto_id, betrag);

    }

    public void neueUeberweisung(int von_Kunde, int von_Konto, int an_Konto, String vzweck, double betrag) throws KontoNichtGefundenException, KontoGesperrtException, NegativerBetragException, BenutzerNichtGefundenException {
        getKontoOfKundeById(von_Kunde, von_Konto);
        kontoDaoLayer.neueUeberweisung(von_Konto, an_Konto, vzweck, betrag);
    }

    public void entferneKontenOfKunde(int kunden_id) throws BenutzerNichtGefundenException {
        kontoDaoLayer.entferneKontenOfKunde(kunden_id);
    }

    public void entferneKontoById(int id) throws KontoNichtGefundenException {
        kontoDaoLayer.entferneKontoById(id);
    }
}