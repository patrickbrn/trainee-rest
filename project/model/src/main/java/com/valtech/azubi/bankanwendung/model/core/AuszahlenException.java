package com.valtech.azubi.bankanwendung.model.core;

public class AuszahlenException extends Exception {

    public AuszahlenException(String msg) {
        super("Auszahlen nicht möglich: "+msg);
    }

}
