package org.HappyBank.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Administrator {

    //Attributes
    private String adminFullName;
    private String adminNIF;
    private String adminSSN;
    private int adminEmployeeID;
    private String adminBankName;
    private BigDecimal adminSalary;

    //Empty constructor
    public Administrator() {
    }

    //Administrator Constructor
    public Administrator(int adminEmployeeID, String adminFullName, String adminBankName, String adminNIF, String adminSSN, BigDecimal adminSalary) {
        this.adminEmployeeID = adminEmployeeID;
        this.adminFullName = adminFullName;
        this.adminBankName = adminBankName;
        this.adminNIF = adminNIF;
        this.adminSSN = adminSSN;
        this.adminSalary = adminSalary;
    }

    //Setters
    public void setAdminFullName(String adminFullName) {
        this.adminFullName = adminFullName;
    }

    public void setAdminNIF(String adminNIF) {
        this.adminNIF = adminNIF;
    }

    public void setAdminSSN(String adminSSN) {
        this.adminSSN = adminSSN;
    }

    public void setAdminEmployeeID(int adminEmployeeID) {
        this.adminEmployeeID = adminEmployeeID;
    }

    public void setAdminBankName(String adminBankName) {
        this.adminBankName = adminBankName;
    }

    public void setAdminSalary(BigDecimal adminSalary) {
        this.adminSalary = adminSalary;
    }

    //Getters
    public String getAdminFullName() {
        return adminFullName;
    }

    public String getAdminNIF() {
        return adminNIF;
    }

    public String getAdminSSN() {
        return adminSSN;
    }

    public int getAdminEmployeeID() {
        return adminEmployeeID;
    }

    public String getAdminBankName() {
        return adminBankName;
    }

    public BigDecimal getAdminSalary() {
        return adminSalary;
    }

    //ToString
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Administrator {ID: " + adminEmployeeID + " , FullName: " + adminFullName + " , BankName: " + adminBankName +
                " , NIF: " + adminNIF + " , SSN: " + adminSSN + " , Salary: " + adminSalary.setScale(2, RoundingMode.HALF_UP) + "}");
        return sb.toString();
    }
}
