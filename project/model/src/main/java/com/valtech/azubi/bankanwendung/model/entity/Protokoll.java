package com.valtech.azubi.bankanwendung.model.entity;

import com.valtech.azubi.bankanwendung.model.core.TransaktionsTyp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@NamedQuery(name = "getProtokollByID", query = "from Protokoll p where p.p_id=:id")
@Entity
@Table(name = "protokoll")

public class Protokoll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "P_ID")
    private int p_id;
    @OneToMany(mappedBy = "protokoll")
    private List<Eintrag> eintraege = new ArrayList<>();


    public Protokoll() {
    }

    public Eintrag addEintrag(Konto vonKonto, Konto zielkonto, TransaktionsTyp bezeichnung, String vzweck, Double betrag) {
        Eintrag e = new Eintrag(this, vonKonto, zielkonto, bezeichnung, vzweck, betrag);
        eintraege.add(e);
        return e;

    }

    public List<Eintrag> getEintraege() {
        return eintraege;
    }
}
