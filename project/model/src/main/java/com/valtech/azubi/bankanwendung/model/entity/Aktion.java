package com.valtech.azubi.bankanwendung.model.entity;


import com.valtech.azubi.bankanwendung.model.core.AktionsTyp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "aktion")
public class Aktion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AKTIONS_ID")
    private int aktions_id;

    @Enumerated(EnumType.STRING)
    @Column(name = "AKTIONS_TYP")
    private AktionsTyp aktionsTyp;
    @Column(name = "GRUND")
    private String grund;
    @Column(name = "DATUM")
    private Date datum;
    @Column(name = "KUNDE")
    private int betroffener_kunde;
    @Column(name = "KONTO")
    private String betroffenes_konto;
    @ManyToOne
    private Angestellter operator;

    public Aktion(AktionsTyp aktionsTyp, String grund, int betroffener_kunde, String betroffenes_konto, Angestellter operator) {
        this.aktionsTyp = aktionsTyp;
        this.grund = grund;
        this.datum = new Date();
        this.betroffener_kunde = betroffener_kunde;
        this.betroffenes_konto = betroffenes_konto;
        this.operator = operator;
    }
}
