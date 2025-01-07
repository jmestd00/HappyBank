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
     * NIF del propietario.
     */
    private final String ownerNIF;
    /**
     * Saldo de la cuenta.
     */
    private BigDecimal balance;
    /**
     * Conexión a la base de datos
     */
    private final AccountRepositoryImpl accountRepository;
    
    //Constructors
    
    /**
     * Constructor con parámetros.
     *
     * @param IBAN     Número de cuenta.
     * @param ownerNIF NIF del propietario.
     * @param balance  Saldo de la cuenta.
     */
    public Account(String IBAN, String ownerNIF, BigDecimal balance) {
        accountRepository = new AccountRepositoryImpl();
        this.IBAN = IBAN;
        this.ownerNIF = ownerNIF;
        this.balance = balance;
        
        accountRepository.add(this);
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
     * Devuelve el NIF del propietario.
     *
     * @return NIF del propietario.
     */
    public String getOwnerNIF() {
        return ownerNIF;
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
    
    
    //Overrides
    /**
     * Devuelve una cadena con la información de la cuenta.
     *
     * @return Cadena con la información de la cuenta.
     */
    @Override
    public String toString() {
        return "Account " + IBAN + " , Owner NIF: " + ownerNIF + " , Balance: " + balance.setScale(2, RoundingMode.HALF_UP);
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
