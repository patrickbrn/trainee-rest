package com.valtech.azubi.bankanwendung.model.dto;

import com.valtech.azubi.bankanwendung.model.core.Rolle;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@XmlRootElement(name = "session")
@XmlAccessorType(XmlAccessType.FIELD)
public class LogInSession {

    @XmlElement
    private String sessionID;
    @XmlElement
    private Date endDate = new Date();
    @XmlElement
    private BenutzerDTO vonBenutzer;
    @XmlElement
    private String ipAdress;




    public LogInSession() {
    }

    public LogInSession(String sessionID, BenutzerDTO vonBenutzer, String ipAdress) {
        this.sessionID = sessionID;
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(endDate);
        if (!vonBenutzer.getRolle().equals(Rolle.ANGESTELLTER)) {
            cal.add(Calendar.SECOND, 180);
        } else {
            cal.add(Calendar.MINUTE, 15);
        }
        endDate = cal.getTime();
        this.vonBenutzer = vonBenutzer;
        this.ipAdress = ipAdress;

    }

    public BenutzerDTO getVonBenutzer() {
        return vonBenutzer;
    }

    public void setVonBenutzer(BenutzerDTO vonBenutzer) {
        this.vonBenutzer = vonBenutzer;
    }

    public String getIpAdress() {
        return ipAdress;
    }

    public void setIpAdress(String ipAdress) {
        this.ipAdress = ipAdress;
    }



    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


}
