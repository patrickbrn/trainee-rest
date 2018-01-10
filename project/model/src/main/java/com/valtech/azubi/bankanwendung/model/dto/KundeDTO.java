package com.valtech.azubi.bankanwendung.model.dto;

import com.valtech.azubi.bankanwendung.model.entity.Konto;
import com.valtech.azubi.bankanwendung.model.entity.Kunde;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class KundeDTO {
    private int benutzerNr;
    private String anmeldename;
    private String vorname;
    private String name;
    private String gebDatum;
    private List<KontoDTO> kontenDTO = new ArrayList<>();

    public KundeDTO(Kunde kunde) {
        this.benutzerNr = kunde.getBenutzerNr();
        this.anmeldename = kunde.getAnmeldename();
        this.vorname = kunde.getVorname();
        this.name = kunde.getName();
        this.gebDatum = String.valueOf(kunde.getGebdatum());
        setKontenDTO(kunde.getKonten());
    }

    public List<KontoDTO> getKontenDTO() {
        return kontenDTO;
    }

    public void setKontenDTO(List<Konto> konten) {
        for (Konto k : konten) {
            kontenDTO.add(new KontoDTO(k));
        }
    }
}