package com.valtech.azubi.bankanwendung.model.entity;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(name = Angestellter.GET_ANGESTELLTER_BY_NUMMER, query = "from Angestellter a where a.benutzerNr=:nr"),
        @NamedQuery(name = Angestellter.GET_ANGESTELLTER_BY_ANMELDENAMEN, query = "from Angestellter a where a.anmeldename=:anmeldename")

})
@Entity
@Table(name = "angestellter")
public class Angestellter extends Benutzer {

    @AttributeOverrides({
            @AttributeOverride(name = "anmeldename", column = @Column(name = "ANMELDENAME")),
            @AttributeOverride(name = "name", column = @Column(name = "NAME")),
            @AttributeOverride(name = "vorname", column = @Column(name = "VORNAME")),
            @AttributeOverride(name = "gebdatum", column = @Column(name = "GEB_DATUM")),
            @AttributeOverride(name = "code", column = @Column(name = "CODE"))
    })

    public static final String GET_ANGESTELLTER_BY_ANMELDENAMEN = "getAngestellteByAnmeldenamen";
    public static final String GET_ANGESTELLTER_BY_NUMMER = "getPersonByID";
    @Column(name = "ROLLE")
    private String rolle;

    public String getRolle() {
        return rolle;
    }

    public void setRolle(String rolle) {
        this.rolle = rolle;
    }
}
