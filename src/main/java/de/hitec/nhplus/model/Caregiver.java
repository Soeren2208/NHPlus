package de.hitec.nhplus.model;

import de.hitec.nhplus.utils.DateConverter;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

/**
 * Caregivers work in a nursing home and take care of patients.
 */
public class Caregiver extends Person {
    private final SimpleLongProperty cid;
    private final SimpleStringProperty phoneNumber;
    private LocalDate inactiveSince;


    /**
     * Constructor to initiate an object of class <code>Caregiver</code> with the given parameters.
     * Use this constructor to initiate objects which are not persisted yet, because they will not have a caregiver id (cid).
     *
     * @param firstName     First name of the caregiver.
     * @param surName       Last name of the caregiver.
     * @param phoneNumber   Phone number of the caregiver.
     * @param inactiveSince Date since the caregiver is inactive (can be null if active).
     */
    public Caregiver(String firstName, String surName, String phoneNumber, LocalDate inactiveSince) {
        super(firstName, surName);
        this.cid = new SimpleLongProperty();
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.inactiveSince = inactiveSince;
    }

    /**
     * Constructor to initiate an object of class <code>Caregiver</code> with the given parameters.
     * Use this constructor to initiate objects which are already persisted and have a caregiver id (cid).
     *
     * @param cid           Caregiver id.
     * @param firstName     First name of the caregiver.
     * @param surName       Last name of the caregiver.
     * @param phoneNumber   Phone number of the caregiver.
     * @param inactiveSince Date since the caregiver is inactive (can be null if active).
     */
    public Caregiver(long cid, String firstName, String surName, String phoneNumber, LocalDate inactiveSince) {
        super(firstName, surName);
        this.cid = new SimpleLongProperty(cid);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.inactiveSince = inactiveSince;
    }


    /**
     * Sets the phone number of the caregiver.
     *
     * @param phoneNumber New phone number.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }


    /**
     * Returns the caregiver id.
     *
     * @return Caregiver id.
     */
    public long getCid() {
        return cid.get();
    }

    /**
     * Returns the date since the caregiver is inactive.
     *
     * @return <code>LocalDate</code> when the caregiver became inactive or <code>null</code> if still active.
     */
    public LocalDate getInactiveSince() {
        return inactiveSince;
    }

    /**
     * Returns the current status of the caregiver as a string.
     * Either "Active" or "Inactive since [date]".
     *
     * @return Status as a string.
     */
    public String getStatus() {
        if (inactiveSince == null) {
            return "Aktiv";
        } else {
            return "Inaktiv seit " + DateConverter.convertLocalDateToString(inactiveSince);
        }
    }

    /**
     * Sets the date when the caregiver became inactive.
     *
     * @param inactiveSince Inactive date to set.
     */
    public void setInactiveSince(LocalDate inactiveSince) {
        this.inactiveSince = inactiveSince;
    }

    /**
     * Returns the phone number of the caregiver.
     *
     * @return Phone number as a string.
     */
    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    /**
     * Returns whether the caregiver is currently active.
     *
     * @return <code>true</code> if active, <code>false</code> if inactive.
     */
    public boolean isActive() {
        return getInactiveSince() == null;
    }


}
