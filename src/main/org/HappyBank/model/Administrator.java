package org.HappyBank.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Clase que representa un administrador.
 */
public class Administrator {
    //Attributes
    /**
     * Nombre del administrador.
     */
    private final String name;
    /**
     * Apellidos del administrador.
     */
    private final String surname;
    /**
     * NIF del administrador.
     */
    private final String NIF;
    /**
     * Número de la seguridad social del administrador.
     */
    private final String SSN;
    /**
     * Salario del administrador.
     */
    private BigDecimal salary;
    /**
     * Nombre del banco.
     */
    private final String bank;
    
    
    //Constructors
    /**
     * Constructor con parámetros.
     * @param name Nombre del administrador.
     * @param surname Apellidos del administrador.
     * @param NIF NIF del administrador.
     * @param SSN SSN del administrador.
     * @param salary Salario del administrador.
     * @param bank Nombre del banco.
     */
    public Administrator(String name, String surname, String NIF, String SSN, BigDecimal salary, String bank) {
        this.name = name;
        this.surname = surname;
        this.NIF = NIF;
        this.SSN = SSN;
        this.salary = salary;
        this.bank = bank;
    }
    
    
    //Getters
    /**
     * Devuelve el nombre del administrador.
     * @return Nombre del administrador.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Devuelve los apellidos del administrador.
     * @return Apellidos del administrador.
     */
    public String getSurname() {
        return surname;
    }
    
    /**
     * Devuelve el DNI del administrador.
     * @return DNI del administrador.
     */
    public String getNIF() {
        return NIF;
    }
    
    /**
     * Devuelve el Número de la seguridad social del administrador.
     * @return SSN del administrador.
     */
    public String getSSN() {
        return SSN;
    }
    
    /**
     * Devuelve el salario del administrador.
     * @return Salario del administrador.
     */
    public BigDecimal getSalary() {
        return salary;
    }
    
    /**
     * Devuelve el nombre del banco.
     * @return Nombre del banco.
     */
    public String getBank() {
        return bank;
    }
    
    
    //Setters
    /**
     * Establece el salario del administrador.
     * @param salary Nombre del administrador.
     */
    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }
    
    
    //Overrides
    /**
     * Devuelve una cadena con la información del administrador.
     * @return Cadena con la información del administrador.
     */
    @Override
    public String toString() {
        return "Administrator " + name + " " + surname + ": " + ", BankName: " + bank + " , NIF: " + NIF + " , SSN: " + SSN + " , Salary: " + salary.setScale(2, RoundingMode.HALF_UP);
    }
}
