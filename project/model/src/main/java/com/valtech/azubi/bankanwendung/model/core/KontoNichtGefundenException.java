package com.valtech.azubi.bankanwendung.model.core;

public class KontoNichtGefundenException extends Exception{

    public KontoNichtGefundenException(String msg) {
        super("Konto nicht gefunden: "+msg);
    }

    public KontoNichtGefundenException(String message, Throwable cause) {
        super(message, cause);
    }

    public KontoNichtGefundenException(Throwable cause) {
        super(cause);
    }

    public KontoNichtGefundenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}