package com.valtech.azubi.bankanwendung.model.dto;

import com.valtech.azubi.bankanwendung.model.core.Kontomodell;
import com.valtech.azubi.bankanwendung.model.entity.Konto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class KontoDTO {

    @XmlElement
    private int kontoNr;
    @XmlElement
    private double saldo;
    @XmlElement
    private boolean gesperrt;
    private Kontomodell modell;

    public KontoDTO() {
    }

    public KontoDTO(Konto konto) {
        kontoNr = konto.getKontoNr();
        this.saldo = konto.getSaldo();
        this.gesperrt = konto.isGesperrt();
        modell=Enum.valueOf(Kontomodell.class,konto.getClass().getSimpleName().toUpperCase());
    }

    public Kontomodell getModell() {
        return modell;
    }

    public void setModell(Kontomodell modell) {
        this.modell = modell;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public void setGesperrt(boolean gesperrt) {
        this.gesperrt = gesperrt;
    }

    public int getKontoNr() {
        return kontoNr;
    }

    public void setKontoNr(int kontoNr) {
        this.kontoNr = kontoNr;
    }

    public double getSaldo() {
        return saldo;
    }

    public boolean isGesperrt() {
        return gesperrt;
    }

    @Override
    public String toString() {
        return "OutKonto{" +
                "kontoNr=" + kontoNr +
                ", saldo=" + saldo +
                ", gesperrt=" + gesperrt +
                '}';
    }
}
