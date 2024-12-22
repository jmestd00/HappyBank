package org.HappyBank.model;

public class Client {
    //Attributes
    private String name;
    private String surname;
    private String NIF;
    private String email;
    private String phone;
    private String address;
    private String bank;

    
    //Constructors
    public Client() {}

    public Client(String name, String surname, String NIF, String email, String phone, String address, String bank) {
        this.NIF = NIF;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.bank = bank;
    }

    
    //Setters
    public void setName(String name) {
        this.name = name;
    }
    
    public void setSurname(String name) {
        this.surname = surname;
    }

    public void setNIF(String NIF) {
        this.NIF = NIF;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    
    //Getters
    public String getName() {
        return name;
    }
    
    public String getSurname() {return surname;}

    public String getNIF() {
        return NIF;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    //To string method
    public String toString() {
        return name + " " + surname + " , BankName: " + bank + " , DNI: " + NIF + " , Email: " + email + " , Phone Number: " +
                phone + " , Address: " + address;
    }
}
