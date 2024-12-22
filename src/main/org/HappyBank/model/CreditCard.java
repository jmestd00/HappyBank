package org.HappyBank.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CreditCard {
    //Attributes
    private String number;
    private String IBAN;
    private LocalDate expirationDate;
    private int CVV;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    //Constructors
    public CreditCard() {}

    public CreditCard(String number, String IBAN, LocalDate expirationDate, int CVV) {
        this.number = number;
        this.IBAN = IBAN;
        this.expirationDate = expirationDate;
        this.CVV = CVV;
    }

    
    //Setters
    public void setNumber(String number) {
        this.number = number;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setCVV(int CVV) {
        this.CVV = CVV;
    }

    
    //Getters
    public String getNumber() {
        return number;
    }

    public String getIBAN() {
        return IBAN;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public int getCVV() {
        return CVV;
    }

    //ToString
    public String toString() {
        return "Credit Card " + number + ": IBAN: " + IBAN + " , Expiration Date: " +
                expirationDate.format(formatter) + " , CVV: " + CVV;
    }
}
