package com.valtech.azubi.bankanwendung.model.entity;

import com.valtech.azubi.bankanwendung.model.core.AuszahlenException;
import com.valtech.azubi.bankanwendung.model.core.NegativerBetragException;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(name = "getKontoByID", query = "from Konto k where k.KontoNr = :KontoNr")
})
@Entity
@Table(name = "konto")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Konto {
    public static final String SPARKONTO = "Sparkonto";
    public static final String GIROKONTO = "Girokonto";
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "KONTO_NR")
    private int KontoNr;
    @Column(name = "SALDO")
    private double saldo;
    @Column(name = "GESPERRT")
    private boolean gesperrt;
    @ManyToOne
    private Kunde kunde;
    @OneToOne
    private Protokoll protokoll;

    public Konto() {
        protokoll = new Protokoll();

    }

    public Kunde getKunde() {
        return kunde;
    }

    public void setKunde(Kunde k) {
        kunde = k;
        k.hinzufuegeKonto(this);
    }

    public Protokoll getProtokoll() {
        return protokoll;
    }

    public void addBetrag(double betrag) throws NegativerBetragException {

        if (betrag >= 0) {
            setSaldo(getSaldo() + betrag);
        } else {
            throw new NegativerBetragException();
        }

    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " + getKontoNr();
    }

    public void subBetrag(double betrag) throws NegativerBetragException, AuszahlenException {

        if (betrag < 0) {
            throw new NegativerBetragException();
        }

        if (saldo - betrag < 0) {
            throw new AuszahlenException("Bestand " + getSaldo() + "€ ungenügend!\n Betrag " + betrag + "€ nicht abbuchbar");
        }

        saldo -= betrag;
    }

    public int getKontoNr() {
        return KontoNr;
    }

    public void setKontoNr(int kontoNr) {
        KontoNr = kontoNr;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }


    public boolean isGesperrt() {
        return gesperrt;
    }

    public void setGesperrt(boolean gesperrt) {
        this.gesperrt = gesperrt;
    }
}