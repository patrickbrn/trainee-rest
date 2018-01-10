package com.valtech.azubi.bankanwendung.model.core;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BenutzerNichtGefundenException extends Exception{

    public BenutzerNichtGefundenException(String message) {
        super(message);
    }
}