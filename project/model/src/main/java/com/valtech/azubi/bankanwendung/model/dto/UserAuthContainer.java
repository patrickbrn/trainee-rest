package com.valtech.azubi.bankanwendung.model.dto;

import com.valtech.azubi.bankanwendung.model.core.Rolle;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserAuthContainer {

    @XmlElement
    private String username;
    @XmlElement
    private String hashPIN;
    @XmlElement
    private Rolle rolle;


    public UserAuthContainer() {
    }


    public UserAuthContainer(String username, String hashPIN, Rolle rolle) {
        this.username = username;
        this.hashPIN = hashPIN;
        this.rolle = rolle;
    }

    public Rolle getRolle() {
        return rolle;
    }

    public void setRolle(Rolle rolle) {
        this.rolle = rolle;
    }

    public void setHashPIN(String hashPIN) {
        this.hashPIN = hashPIN;
    }

    @Override
    public String toString() {
        return "UserAuthContainer{" +
                "username='" + username + '\'' +
                ", hashPIN='" + hashPIN + '\'' +
                ", type='" + rolle+ '\'' +
                '}';
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getHashPIN() {
        return hashPIN;
    }
}
