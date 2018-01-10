package com.valtech.azubi.bankanwendung.model.core;

public class NegativerBetragException extends Exception{

    public NegativerBetragException() {
        super("Negative Beträge sind nicht erlaubt!");

    }

    public NegativerBetragException(String message) {
        super(message);
    }
}
