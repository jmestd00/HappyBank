package org.HappyBank.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CreditCard {

    //Attributes
    private String cardNumber;
    private String cardIBAN;
    private LocalDate cardExpirationDate;
    private int cardCVV;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    //Empty constructor
    public CreditCard() {
    }

    //CreditCard Constructor
    public CreditCard(String cardNumber, String cardIBAN, LocalDate cardExpirationDate, int cardCVV) {
        this.cardNumber = cardNumber;
        this.cardIBAN = cardIBAN;
        this.cardExpirationDate = cardExpirationDate;
        this.cardCVV = cardCVV;
    }

    //Setters
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setCardIBAN(String cardIBAN) {
        this.cardIBAN = cardIBAN;
    }

    public void setCardExpirationDate(LocalDate cardExpirationDate) {
        this.cardExpirationDate = cardExpirationDate;
    }

    public void setCardCVV(int cardCVV) {
        this.cardCVV = cardCVV;
    }

    //Getters
    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardIBAN() {
        return cardIBAN;
    }

    public LocalDate getCardExpirationDate() {
        return cardExpirationDate;
    }

    public int getCardCVV() {
        return cardCVV;
    }

    //ToString
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Credit Card {Card Number: " + cardNumber + " , IBAN: " + cardIBAN + " , Expiration Date: " +
                cardExpirationDate.format(formatter) + " , CVV: " + cardCVV + "}");
        return sb.toString();
    }
}
