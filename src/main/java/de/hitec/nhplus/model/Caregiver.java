package de.hitec.nhplus.model;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class Caregiver extends Person{
    private SimpleStringProperty telNumber;
    private SimpleLongProperty cgID;

    public Caregiver(long cgID,String firstName, String surname, String telNumber) {
        super(firstName, surname);
        this.cgID = new SimpleLongProperty(cgID);
        this.telNumber = new SimpleStringProperty(telNumber);
    }

    public Caregiver(String firstName, String surname, String telNumber) {
        super(firstName, surname);
        this.telNumber = new SimpleStringProperty(telNumber);
    }

    public long getCgID() {
        return cgID.get();
    }

    public SimpleLongProperty cgIDProperty() {
        return cgID;
    }

    public void setCgID(long cgID) {
        this.cgID.set(cgID);
    }

    public String getTelNumber() {
        return telNumber.get();
    }

    public SimpleStringProperty telNumberProperty() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber.set(telNumber);
    }

    @Override
    public String toString() {
        return "Pfleger" + "\nMNID: " + this.cgID +
                "\nFirstname: " + this.getFirstName() +
                "\nSurname: " + this.getSurname() +
                "\nBirthday: " + this.telNumber +
                "\n";
    }
}