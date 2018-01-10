package com.valtech.azubi.bankanwendung.model.entity;

import com.valtech.azubi.bankanwendung.model.core.TransaktionsTyp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "eintrag")
public class Eintrag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "E_ID")

    private int e_id;

    @ManyToOne
    private Protokoll protokoll;

    @Column(name = "VON_KONTO")
    private int vonKonto;

    @Column(name = "ZIEL_KONTO")
    private int zielKonto;

    @Enumerated(EnumType.STRING)
    @Column(name = "BEZEICHNUNG")
    private TransaktionsTyp bezeichnung;
    @Column(name = "VERWENDUNGSZWECK")
    private String vzweck;
    @Column(name = "BETRAG")
    private Double betrag;
    @Column(name = "DATUM")

    private Date datum;

    public Eintrag(Protokoll protokoll, Konto vonKonto, Konto zielKonto, TransaktionsTyp bezeichnung, String vzweck, Double betrag) {
        this.protokoll = protokoll;

        if (vonKonto != null) {
            this.vonKonto = vonKonto.getKontoNr();
        }
        if (zielKonto != null) {
            this.zielKonto = zielKonto.getKontoNr();
        }

        this.bezeichnung = bezeichnung;
        this.vzweck = vzweck;
        this.betrag = betrag;
        this.datum = new Date();
    }

    public Eintrag() {
    }


    public String getVzweck() {
        return vzweck;
    }

    public void setVzweck(String vzweck) {
        this.vzweck = vzweck;
    }

    public int getE_id() {
        return e_id;
    }

    public void setE_id(int e_id) {
        this.e_id = e_id;
    }

    public Protokoll getProtokoll() {
        return protokoll;
    }

    public void setProtokoll(Protokoll protokoll) {
        this.protokoll = protokoll;
    }

    public int getVonKonto() {
        return vonKonto;
    }

    public void setVonKonto(int vonKonto) {
        this.vonKonto = vonKonto;
    }

    public int getZielKonto() {
        return zielKonto;
    }

    public void setZielKonto(int zielKonto) {
        this.zielKonto = zielKonto;
    }

    public TransaktionsTyp getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(TransaktionsTyp bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public Double getBetrag() {
        return betrag;
    }

    public void setBetrag(Double betrag) {
        this.betrag = betrag;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }
}
