package com.valtech.azubi.bankanwendung.model.entity;

import org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "benutzer")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Benutzer {
    @Id
    @Column(name = "B_ID")
    @GeneratedValue(strategy = GenerationType.TABLE)
    private int benutzerNr;
    @Column(name = "ANMELDENAME")
    private String anmeldename;
    @Column(name = "NAME")
    private String name;
    @Column(name = "VORNAME")
    private String vorname;
    @Column(name = "GEB_DATUM")
    private LocalDate gebdatum;
    @Column(name = "CODE")
    private String code;


    public void setBenutzer(String name, String vorname, LocalDate gebdatum, String code) {
        this.name = name;
        this.vorname = vorname;
        this.gebdatum = gebdatum;
        setCode(code);
    }


    public String getAnmeldename() {
        return anmeldename;
    }

    public void setAnmeldename(String anmeldename) {
        this.anmeldename = anmeldename;
    }

    public boolean checkCode(String code) {
        return DigestUtils.sha256Hex(code).equals(this.code);
    }


    public int getBenutzerNr() {
        return benutzerNr;
    }

    public void setBenutzerNr(int benutzerNr) {
        this.benutzerNr = benutzerNr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {

        this.name = name;


    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public LocalDate getGebdatum() {
        return gebdatum;
    }

    public void setGebdatum(LocalDate gebdatum) {
        this.gebdatum = gebdatum;
    }

    public void setGebdatum(String gebdatum) {
        this.gebdatum = LocalDate.parse(gebdatum);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {

        this.code = DigestUtils.sha256Hex(code);
    }


    @Override
    public String toString() {
        return "Benutzer{" +
                "benutzerNr=" + benutzerNr +
                ", anmeldename='" + anmeldename + '\'' +
                ", name='" + name + '\'' +
                ", vorname='" + vorname + '\'' +
                ", gebdatum=" + gebdatum +
                ", code='" + code + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Benutzer benutzer = (Benutzer) o;

        return vorname != null ? vorname.equals(benutzer.vorname) : benutzer.vorname == null;
    }

    @Override
    public int hashCode() {
        return vorname != null ? vorname.hashCode() : 0;
    }
}