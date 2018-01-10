package com.valtech.azubi.bankanwendung.server.configuration;

import com.valtech.azubi.bankanwendung.model.core.Rolle;
import com.valtech.azubi.bankanwendung.server.services.DBService;
import com.valtech.azubi.bankanwendung.server.services.UserService;

import java.time.LocalDate;

public class Testdata {
    LocalDate date = LocalDate.parse("2017-12-12");

    public static void main(String[] args) {

        System.out.println("TEST");

        DBService.openSession();
        Testdata testdata = new Testdata();
        testdata.createUser();
        DBService.closeSession();
    }

    public void createUser() {

        UserService.getInstance().benutzerAnlegen("admin", "bob", date, "1234", "ADMIN", Rolle.ANGESTELLTER);
        UserService.getInstance().benutzerAnlegen("Kunde", "Erster", date, "1111", "KUNDE", Rolle.KUNDE);
    }
}
