package com.valtech.azubi.bankanwendung.model.entity;

import com.valtech.azubi.bankanwendung.model.core.AuszahlenException;
import com.valtech.azubi.bankanwendung.model.core.NegativerBetragException;

import javax.persistence.*;


@Entity
@Table(name = "girokonto")
@AttributeOverrides({
        @AttributeOverride(name = "saldo", column = @Column(name = "SALDO")),
        @AttributeOverride(name = "gesperrt", column = @Column(name = "GESPERRT")),
})
public class Girokonto extends Konto {
    @Column(name = "DISPO")
    private double dispo;


    public void subBetrag(double betrag) throws NegativerBetragException, AuszahlenException {
        if (betrag >= 0) {
            if (getSaldo() + dispo >= betrag) {
                setSaldo(getSaldo() - betrag);
            } else
                throw new AuszahlenException("Bestand " + getSaldo() + "€ mit möglichen Dispo: " + getDispo() + " ungenügend!\nBetrag " + betrag + "€ zu hoch");
        } else throw new NegativerBetragException();
    }


    public double getDispo() {
        return dispo;
    }

    public void setDispo(double dispo) {
        this.dispo = dispo;
    }
}
