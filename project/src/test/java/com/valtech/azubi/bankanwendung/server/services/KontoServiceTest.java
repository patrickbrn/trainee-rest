package com.valtech.azubi.bankanwendung.server.services;

import com.valtech.azubi.bankanwendung.model.core.KontoNichtGefundenException;
import com.valtech.azubi.bankanwendung.model.entity.Konto;
import com.valtech.azubi.bankanwendung.server.dao.KontoDaoLayer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.TestCase.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.when;

class KontoServiceTest {
    @Mock
    private KontoDaoLayer daoMock;
    @InjectMocks
    private KontoService service;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createKonto() {


    }

    @Test
    void sperreKontoById() {
    }

    @Test
    void entsperreKontoById() {
    }

    @Test
    void getKontoByID() throws KontoNichtGefundenException {
        when(daoMock.getKontoByID(anyInt())).thenReturn(new Konto());
        assertEquals(service.getKontoByID(anyInt()),notNull());
    }

    @Test
    void getKontoOfKundeById() {
    }

    @Test
    void einzahlenById() {
    }

    @Test
    void auszahlenById() {
    }

    @Test
    void neueUeberweisung() {
    }

    @Test
    void entferneKontenOfKunde() {
    }

    @Test
    void entferneKontoById() {
    }
}