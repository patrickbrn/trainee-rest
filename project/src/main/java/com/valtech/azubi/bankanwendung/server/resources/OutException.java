package com.valtech.azubi.bankanwendung.server.resources;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class OutException extends Exception {
    private int status;
    private int code;
    private String message;
    private String devMessage;

    public OutException(String message, int status, int code, String message1, String devMessage) {
        super(message);
        this.status = status;
        this.code = code;
        this.message = message1;
        this.devMessage = devMessage;
    }

}