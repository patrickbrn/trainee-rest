package com.valtech.azubi.bankanwendung.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "sparkonto")
@AttributeOverrides({
        @AttributeOverride(name = "saldo", column = @Column(name = "SALDO")),
        @AttributeOverride(name = "gesperrt", column = @Column(name = "GESPERRT")),
})
public class Sparkonto extends Konto {

  /*  @OneToOne
    private Protokoll protokoll;*/


}