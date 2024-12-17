package org.HappyBank.model;

public class Client {

    //Attributes
    private String clientName;
    private String clientFirstSurname;
    private String clientSecondSurname;
    private String clientDNI;
    private String clientEmail;
    private int clientPhoneNumber;
    private String clientAddress;

    //Empty Client Constructor
    public Client() {
    }

    //Client Constructor
    public Client(String clientName, String clientFirstSurname, String clientSecondSurname, String clientDNI, String clientEmail, int clientPhoneNumber, String clientAddress) {
        this.clientName = clientName;
        this.clientFirstSurname = clientFirstSurname;
        this.clientSecondSurname = clientSecondSurname;
        this.clientDNI = clientDNI;
        this.clientEmail = clientEmail;
        this.clientPhoneNumber = clientPhoneNumber;
        this.clientAddress = clientAddress;
    }

    //Setters
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setClientFirstSurname(String clientFirstSurname) {
        this.clientFirstSurname = clientFirstSurname;
    }

    public void setClientSecondSurname(String clientSecondSurname) {
        this.clientSecondSurname = clientSecondSurname;
    }

    public void setClientDNI(String clientDNI) {
        this.clientDNI = clientDNI;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public void setClientPhoneNumber(int clientPhoneNumber) {
        this.clientPhoneNumber = clientPhoneNumber;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    //Getters

    public String getClientName() {
        return clientName;
    }

    public String getClientFirstSurname() {
        return clientFirstSurname;
    }

    public String getClientSecondSurname() {
        return clientSecondSurname;
    }

    public String getClientDNI() {
        return clientDNI;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public int getClientPhoneNumber() {
        return clientPhoneNumber;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public String getFullName() {
        return clientName + " " + clientFirstSurname + " " + clientSecondSurname;
    }

    //To string method

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Client {Name: " + this.getFullName() + " , DNI: " + clientDNI + " , Email: " + clientEmail + " , Phone Number: " +
                clientPhoneNumber + " , Address: " + clientAddress + "}");
        return sb.toString();
    }
}
