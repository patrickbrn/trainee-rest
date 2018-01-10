package com.valtech.azubi.bankanwendung.model.entity;

import com.valtech.azubi.bankanwendung.model.core.TransaktionsTyp;

import javax.persistence.*;
import java.util.Date;

@NamedQueries({
        @NamedQuery(name = "getTransaktionen", query = "from Transaktion"),
})
@Entity
@Table(name = "transaktion")
public class Transaktion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRANS_ID")
    private int trans_id;
    @ManyToOne
    private Konto konto;
    @ManyToOne
    private Konto zielkonto;
    @Enumerated(EnumType.STRING)
    @Column(name = "BEZEICHNUNG")
    private TransaktionsTyp bezeichnung;
    @Column(name = "VERWENDUNGSZWECK")
    private String vzweck;
    @Column(name = "BETRAG")
    private Double betrag;
    @Column(name = "DATUM")
    private Date datum;

    public Transaktion(Konto konto, Konto zielkonto, TransaktionsTyp bezeichnung, String vzweck, Double betrag, Date datum) {
        this.konto = konto;
        this.zielkonto = zielkonto;
        this.bezeichnung = bezeichnung;
        this.vzweck = vzweck;
        this.betrag = betrag;
        this.datum = datum;

    }

    public Transaktion() {
    }

    public String getVzweck() {
        return vzweck;
    }

    public void setVzweck(String vzweck) {
        this.vzweck = vzweck;
    }

    public int getTrans_id() {
        return trans_id;
    }

    public void setTrans_id(int trans_id) {
        this.trans_id = trans_id;
    }

    public Konto getZielkonto() {
        return zielkonto;
    }

    public void setZielkonto(Konto zielkonto) {
        this.zielkonto = zielkonto;
    }

    @Override
    public String toString() {
        return "\nTransaktion{" +
                "trans_id=" + trans_id +
                ", konto=" + konto.getKontoNr() +
                ", zielkonto=" + zielkonto.getKontoNr() +
                ", bezeichnung=" + bezeichnung +
                ", betrag=" + betrag +
                "€, datum=" + datum +
                "}\n";
    }

    public Konto getKonto() {
        return konto;
    }

    public void setKonto(Konto konto) {
        this.konto = konto;
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