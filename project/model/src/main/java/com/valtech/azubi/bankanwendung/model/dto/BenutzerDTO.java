package com.valtech.azubi.bankanwendung.model.dto;


import com.valtech.azubi.bankanwendung.model.core.Rolle;
import com.valtech.azubi.bankanwendung.model.entity.Benutzer;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class BenutzerDTO {
    private int benutzerNr;
    private String anmeldename;
    private String vorname;
    private String name;
    private String gebDatum;
    private Rolle rolle;

    public BenutzerDTO(Benutzer benutzer) {
        this.benutzerNr = benutzer.getBenutzerNr();
        this.anmeldename = benutzer.getAnmeldename();
        this.vorname = benutzer.getVorname();
        this.name = benutzer.getName();
        this.gebDatum = String.valueOf(benutzer.getGebdatum());
        this.rolle = Enum.valueOf(Rolle.class, benutzer.getClass().getSimpleName().toUpperCase());
    }


}