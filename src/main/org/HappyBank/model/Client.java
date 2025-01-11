package org.HappyBank.model;

import org.HappyBank.model.repository.ClientRepositoryImpl;

/**
 * Clase que representa un cliente.
 */
public class Client {
    //Attributes
    /**
     * Nombre del cliente.
     */
    private final String name;
    /**
     * Apellidos del cliente.
     */
    private final String surname;
    /**
     * NIF del cliente.
     */
    private final String NIF;
    /**
     * Email del cliente.
     */
    private String email;
    /**
     * Teléfono del cliente.
     */
    private String phone;
    /**
     * Dirección del cliente.
     */
    private String address;
    /**
     * Nombre del banco.
     */
    private final String bank;
    /**
     * Conexión con la base de datos
     */
    private ClientRepositoryImpl clientRepository;
    
    
    //Constructors
    /**
     * Constructor universal de cliente.
     *
     * @param name    Nombre del cliente.
     * @param surname Apellidos del cliente.
     * @param NIF     NIF del cliente.
     * @param email   Email del cliente.
     * @param phone   Teléfono del cliente.
     * @param address Dirección del cliente.
     * @param bank    Nombre del banco.
     */
    public Client(String name, String surname, String NIF, String email, String phone, String address, String bank) {
        clientRepository = new ClientRepositoryImpl();
        this.NIF = NIF;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.bank = bank;
    }
    
    
    //Getters
    /**
     * Devuelve el nombre del cliente.
     *
     * @return Nombre del cliente.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Devuelve los apellidos del cliente.
     *
     * @return Apellidos del cliente.
     */
    public String getSurname() {
        return surname;
    }
    
    /**
     * Devuelve el NIF del cliente.
     *
     * @return NIF del cliente.
     */
    public String getNIF() {
        return NIF;
    }
    
    /**
     * Devuelve el email del cliente.
     *
     * @return Email del cliente.
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Devuelve el teléfono del cliente.
     *
     * @return Teléfono del cliente.
     */
    public String getPhone() {
        return phone;
    }
    
    /**
     * Devuelve la dirección del cliente.
     *
     * @return Dirección del cliente.
     */
    public String getAddress() {
        return address;
    }
    
    /**
     * Devuelve el nombre del banco.
     *
     * @return Nombre del banco.
     */
    public String getBank() {
        return bank;
    }
    
    
    //Setters
    /**
     * Establece el email del cliente.
     *
     * @param email Nombre del cliente.
     */
    public void setEmail(String email) {
        this.email = email;
        clientRepository.update(this);
    }
    
    /**
     * Establece el teléfono del cliente.
     *
     * @param phone Nombre del cliente.
     */
    public void setPhone(String phone) {
        this.phone = phone;
        clientRepository.update(this);
    }
    
    /**
     * Establece la dirección del cliente.
     *
     * @param address Nombre del cliente.
     */
    public void setAddress(String address) {
        this.address = address;
        clientRepository.update(this);
    }
    
    /**
     * Establece la conexión con la base de datos.
     *
     * @param clientRepository Conexión con la base de datos.
     */
    public void setClientRepository(ClientRepositoryImpl clientRepository) {
        this.clientRepository = clientRepository;
    }
    
    //Override
    /**
     * Devuelve una cadena con la información del cliente.
     *
     * @return Cadena con la información del cliente.
     */
    @Override
    public String toString() {
        return name + " " + surname + " , BankName: " + bank + " , DNI: " + NIF + " , Email: " + email + " , Phone Number: " + phone + " , Address: " + address;
    }
}
