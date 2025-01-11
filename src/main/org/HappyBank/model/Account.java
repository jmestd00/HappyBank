package org.HappyBank.model;

import org.HappyBank.model.repository.AccountRepositoryImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Clase que representa una cuenta bancaria.
 */
public class Account {
    //Attributes
    /**
     * Número de cuenta.
     */
    private final String IBAN;
    /**
     * Propietario de la cuenta.
     */
    private final Client owner;
    /**
     * Saldo de la cuenta.
     */
    private BigDecimal balance;
    /**
     * Conexión a la base de datos
     */
    private AccountRepositoryImpl accountRepository;
    
    
    //Constructors
    /**
     * Constructor para crear una cuenta.
     *
     * @param owner Propietario.
     */
    public Account(Client owner) {
        Generator generator = new Generator();
        
        this.accountRepository = new AccountRepositoryImpl();
        this.IBAN = generator.generateUniqueIBAN();
        this.owner = owner;
        this.balance = new BigDecimal(0);
    }
    
    /**
     * Constructor para descargar una cuenta.
     *
     * @param IBAN     Número de cuenta.
     * @param owner NIF del propietario.
     * @param balance  Saldo de la cuenta.
     */
    public Account(String IBAN, Client owner, BigDecimal balance) {
        this.accountRepository = new AccountRepositoryImpl();
        this.IBAN = IBAN;
        this.owner = owner;
        this.balance = balance;
    }
    
    
    //Getters
    /**
     * Devuelve el número de cuenta.
     *
     * @return Número de cuenta.
     */
    public String getIBAN() {
        return IBAN;
    }
    
    /**
     * Devuelve al propietario.
     *
     * @return Propietario.
     */
    public Client getOwner() {
        return owner;
    }
    
    /**
     * Devuelve el saldo de la cuenta.
     *
     * @return Saldo de la cuenta.
     */
    public BigDecimal getBalance() {
        return balance;
    }
    
    
    //Setters
    /**
     * Establece el saldo de cuenta.
     *
     * @param balance Número de cuenta.
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
        accountRepository.update(this);
    }
    
    /**
     * Establece la conexión a la base de datos.
     *
     * @param accountRepository Conexión a la base de datos.
     */
    public void setAccountRepository(AccountRepositoryImpl accountRepository) {
        this.accountRepository = accountRepository;
    }
    
    //Overrides
    /**
     * Devuelve una cadena con la información de la cuenta.
     *
     * @return Cadena con la información de la cuenta.
     */
    @Override
    public String toString() {
        return "Account " + IBAN + " , Owner NIF: " + owner.getNIF() + " , Balance: " + balance.setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Comprueba si dos cuentas son la misma.
     *
     * @param o Objeto a comparar.
     * @return true si es la misma cuenta, false en caso contrario.
     */
    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Account account)) {
            return false;
        }
        
        return IBAN.equals(account.IBAN);
    }
}
