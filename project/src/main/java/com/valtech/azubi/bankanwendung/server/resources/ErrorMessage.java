package com.valtech.azubi.bankanwendung.server.resources;

import lombok.Getter;
import lombok.Setter;

import javax.ws.rs.NotFoundException;

@Setter
@Getter
public class ErrorMessage {
    private int status;
    private int code;
    private String message;
    private String devMessage;

    public ErrorMessage(OutException ex) {
        status = ex.getStatus();
        code = ex.getCode();
        message = ex.getMessage();
        devMessage = ex.getDevMessage();
    }

    public ErrorMessage(NotFoundException nf) {
        status = 404;
        message = nf.getMessage();
    }
}