package org.HappyBank.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Account {
    //Attributes
    private String IBAN;
    private String ownerNIF;
    private BigDecimal balance;

    //Constructors
    public Account() {}

    public Account(String IBAN, String ownerNIF, BigDecimal balance) {
        this.IBAN = IBAN;
        this.ownerNIF = ownerNIF;
        this.balance = balance;
    }

    
    //Setters
    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public void setOwnerNIF(String ownerNIF) {
        this.ownerNIF = ownerNIF;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    
    //Getters
    public String getIBAN() {
        return IBAN;
    }

    public String getOwnerNIF() {
        return ownerNIF;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    
    //Overrides
    public String toString() {
        return "Account " + IBAN + " , Owner NIF: " + ownerNIF + " , Balance: " +
                balance.setScale(2, RoundingMode.HALF_UP);
    }
    
    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Account account)) {
            return false;
        }
        
        return IBAN.equals(account.IBAN);
    }
}
