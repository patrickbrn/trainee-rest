package com.valtech.azubi.bankanwendung.model.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NamedQueries({
        @NamedQuery(name = Kunde.GET_KUNDEN, query = "from Kunde"),
        @NamedQuery(name = Kunde.GET_KUNDEN_BY_NAME, query = "from Kunde k where k.name LIKE :name and k.vorname LIKE :vorname"),
        @NamedQuery(name = Kunde.GET_KUNDEN_BY_KUNDENNUMMER, query = "from Kunde k where k.benutzerNr=:benutzerNr"),
        @NamedQuery(name = "getKundeByFirstName", query = "from Kunde k where k.vorname=:vorname"),
        @NamedQuery(name = "getKundeBySurName", query = "from Kunde k where k.name=:name"),
        @NamedQuery(name = Kunde.GET_KUNDEN_BY_ANMELDENAME, query = "from Kunde k where k.anmeldename=:anmeldename")

})
@Entity
@Table(name = "kunde")
public class Kunde extends Benutzer {
    public static final String GET_KUNDEN = "getKunden";
    public static final String GET_KUNDEN_BY_ANMELDENAME = "getKundeByAnmeldename";
    public static final String GET_KUNDEN_BY_NAME = "getKundeByName";
    public static final String GET_KUNDEN_BY_KUNDENNUMMER = "getKundenByKundennummer";

    @AttributeOverrides({
            @AttributeOverride(name = "anmeldename", column = @Column(name = "ANMELDENAME")),
            @AttributeOverride(name = "name", column = @Column(name = "NAME")),
            @AttributeOverride(name = "vorname", column = @Column(name = "VORNAME")),
            @AttributeOverride(name = "gebdatum", column = @Column(name = "GEB_DATUM")),
            @AttributeOverride(name = "code", column = @Column(name = "CODE"))
    })
    @OneToMany(mappedBy = "kunde")
    private List<Konto> konten = new ArrayList<>();


    public Konto sucheKonto(int kontonr) {

        for (Konto konto : konten) {
            if (konto.getKontoNr() == kontonr) {
                return konto;
            }
        }
        return null;
    }

    public void hinzufuegeKonto(Konto k) {
        konten.add(k);
    }

    public List<Konto> getKonten() {
        return konten;
    }

    public void entferneKonto(Konto k) {
        konten.remove(k);
    }
}
