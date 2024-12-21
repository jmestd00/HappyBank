package org.HappyBank.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Account {

    //Attributes
    private String accountIBAN;
    private String ownerNIF;
    private BigDecimal accountBalance;

    //Empty constructor
    public Account() {
    }

    //Account Constructor
    public Account(String accountIBAN, String ownerNIF, BigDecimal accountBalance) {
        this.accountIBAN = accountIBAN;
        this.ownerNIF = ownerNIF;
        this.accountBalance = accountBalance;
    }

    //Setters
    public void setAccountIBAN(String accountIBAN) {
        this.accountIBAN = accountIBAN;
    }

    public void setOwnerNIF(String ownerNIF) {
        this.ownerNIF = ownerNIF;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    //Getters
    public String getAccountIBAN() {
        return accountIBAN;
    }

    public String getOwnerNIF() {
        return ownerNIF;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    //ToString
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Account {IBAN: " + accountIBAN + " , Owner NIF: " + ownerNIF + " , Balance: " +
                accountBalance.setScale(2, RoundingMode.HALF_UP) + "}");
        return sb.toString();
    }

}
