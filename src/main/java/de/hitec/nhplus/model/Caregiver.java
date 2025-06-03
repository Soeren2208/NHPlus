package de.hitec.nhplus.model;

import de.hitec.nhplus.utils.DateConverter;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public class Caregiver extends Person {
    private final SimpleLongProperty cid;
    private final SimpleStringProperty phoneNumber;
    private LocalDate inactiveSince;


    public Caregiver(String firstName, String surName, String phoneNumber, LocalDate inactiveSince) {
        super(firstName, surName);
        this.cid = new SimpleLongProperty();
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.inactiveSince = inactiveSince;
    }

    public Caregiver(long cid, String firstName, String surName, String phoneNumber, LocalDate inactiveSince) {
        super(firstName, surName);
        this.cid = new SimpleLongProperty(cid);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.inactiveSince = inactiveSince;
    }


    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }


    public long getCid() {
        return cid.get();
    }

    public LocalDate getInactiveSince() {
        return inactiveSince;
    }

    public String getStatus() {
        if (inactiveSince == null) {
            return "Aktiv";
        } else {
            return "Inaktiv seit " + DateConverter.convertLocalDateToString(inactiveSince);
        }
    }

    public void setInactiveSince(LocalDate inactiveSince) {
    this.inactiveSince = inactiveSince;
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public boolean isActive() {
        return getInactiveSince() == null;
    }


}
