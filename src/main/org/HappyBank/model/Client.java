package org.HappyBank.model;

public class Client {

    //Attributes
    private String clientFullName;
    private String clientNIF;
    private String clientEmail;
    private int clientPhoneNumber;
    private String clientAddress;
    private String clientBankName;

    //Empty Client Constructor
    public Client() {
    }

    //Client Constructor
    public Client(String clientFullName, String clientBankName, String clientNIF, String clientEmail, int clientPhoneNumber, String clientAddress) {
        this.clientFullName = clientFullName;
        this.clientNIF = clientNIF;
        this.clientEmail = clientEmail;
        this.clientPhoneNumber = clientPhoneNumber;
        this.clientAddress = clientAddress;
        this.clientBankName = clientBankName;
    }

    //Setters
    public void setClientFullName(String clientFullName) {
        this.clientFullName = clientFullName;
    }

    public void setclientNIF(String clientNIF) {
        this.clientNIF = clientNIF;
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

    public String getFullName() {
        return clientFullName;
    }

    public String getclientNIF() {
        return clientNIF;
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

    //To string method

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Client {Name: " + clientFullName + " , BankName: " + clientBankName + " , DNI: " + clientNIF + " , Email: " + clientEmail + " , Phone Number: " +
                clientPhoneNumber + " , Address: " + clientAddress + "}");
        return sb.toString();
    }
}
