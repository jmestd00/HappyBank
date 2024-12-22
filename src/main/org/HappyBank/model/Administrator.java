package org.HappyBank.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Administrator {
    //Attributes
    private String name;
    private String surname;
    private String NIF;
    private String SSN;
    private BigDecimal salary;
    private String bank;

    
    //Constructors
    public Administrator() {}

    public Administrator(String name, String surname, String NIF, String SSN, BigDecimal salary, String bank) {
        this.name = name;
        this.surname = surname;
        this.bank = bank;
        this.NIF = NIF;
        this.SSN = SSN;
        this.salary = salary;
    }

    
    //Setters
    public void setName(String name) {
        this.name = name;
    }
    
    public void setSurname(String surname) {this.surname = surname;}

    public void setNIF(String NIF) {
        this.NIF = NIF;
    }

    public void setSSN(String SSN) {
        this.SSN = SSN;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }
    
    public void setBank(String bank) {
        this.bank = bank;
    }

    
    //Getters
    public String getName() {
        return name;
    }

    public String getNIF() {
        return NIF;
    }

    public String getSSN() {
        return SSN;
    }
    
    public BigDecimal getSalary() {
        return salary;
    }
    
    public String getBank() {
        return bank;
    }

    
    //Overrides
    public String toString() {
        return "Administrator " + name + " " + surname + ": " + ", BankName: " + bank +
                " , NIF: " + NIF + " , SSN: " + SSN + " , Salary: " + salary.setScale(2, RoundingMode.HALF_UP);
    }
}
