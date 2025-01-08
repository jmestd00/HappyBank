package org.HappyBank.model;

import org.HappyBank.model.repository.AdministratorRepositoryImpl;

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
     /**
      * Conexión con la base de datos
      */
     private final AdministratorRepositoryImpl administratorRepository;
    
    
    //Constructors
     /**
      * Constructor para crear un administrador.
      *
      * @param name     Nombre del administrador.
      * @param surname  Apellidos del administrador.
      * @param NIF      NIF del administrador.
      * @param SSN      SSN del administrador.
      * @param salary   Salario del administrador.
      * @param bank     Nombre del banco.
      * @param password Contraseña del administrador.
      */
     public Administrator(String name, String surname, String NIF, String SSN, BigDecimal salary, String bank, String password) {
         administratorRepository = new AdministratorRepositoryImpl();
         this.name = name;
         this.surname = surname;
         this.NIF = NIF;
         this.SSN = SSN;
         this.salary = salary;
         this.bank = bank;
         
         administratorRepository.add(this);
         administratorRepository.changePassword(NIF, password);
     }
     
    /**
     * Constructor para descargar un administrador.
     *
     * @param name    Nombre del administrador.
     * @param surname Apellidos del administrador.
     * @param NIF     NIF del administrador.
     * @param SSN     SSN del administrador.
     * @param salary  Salario del administrador.
     * @param bank    Nombre del banco.
     */
    public Administrator(String name, String surname, String NIF, String SSN, BigDecimal salary, String bank) {
        administratorRepository = new AdministratorRepositoryImpl();
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
     *
     * @return Nombre del administrador.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Devuelve los apellidos del administrador.
     *
     * @return Apellidos del administrador.
     */
    public String getSurname() {
        return surname;
    }
    
    /**
     * Devuelve el DNI del administrador.
     *
     * @return DNI del administrador.
     */
    public String getNIF() {
        return NIF;
    }
    
    /**
     * Devuelve el Número de la seguridad social del administrador.
     *
     * @return SSN del administrador.
     */
    public String getSSN() {
        return SSN;
    }
    
    /**
     * Devuelve el salario del administrador.
     *
     * @return Salario del administrador.
     */
    public BigDecimal getSalary() {
        return salary;
    }
    
    /**
     * Establece el salario del administrador.
     *
     * @param salary Nombre del administrador.
     */
    public void setSalary(BigDecimal salary) {
        this.salary = salary;
        administratorRepository.update(this);
    }
    
    
    //Setters
    /**
     * Devuelve el nombre del banco.
     *
     * @return Nombre del banco.
     */
    public String getBank() {
        return bank;
    }
    
    
    //Overrides
    /**
     * Devuelve una cadena con la información del administrador.
     *
     * @return Cadena con la información del administrador.
     */
    @Override
    public String toString() {
        return "Administrator " + name + " " + surname + ": " + ", BankName: " + bank + " , NIF: " + NIF + " , SSN: " + SSN + " , Salary: " + salary.setScale(2, RoundingMode.HALF_UP);
    }
}
