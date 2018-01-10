package com.valtech.azubi.bankanwendung.model.core;

import com.valtech.azubi.bankanwendung.model.entity.Konto;

public class KontoGesperrtException extends Exception{
    public KontoGesperrtException(Konto k) {
        super("Konto mit der Kontonummer: "+k.getKontoNr()+" gesperrt! Keine Transaktionen möglich!");

    }
}