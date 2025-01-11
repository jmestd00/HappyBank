package org.HappyBank.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Clase que representa una tarjeta de crédito.
 */
public class CreditCard {
    //Attributes
    /**
     * Número de la tarjeta.
     */
    private final String number;
    /**
     * Cuenta asociada a la tarjeta.
     */
    private final Account account;
    /**
     * Fecha de caducidad de la tarjeta.
     */
    private final LocalDate expirationDate;
    /**
     * CVV de la tarjeta.
     */
    private final String CVV;
    /**
     * Formateador de fechas en día/mes/año.
     */
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
    
    
    //Constructors
    /**
     * Constructor para crear una tarjeta.
     *
     * @param account Cuenta asociada a la tarjeta.
     */
    public CreditCard(Account account) {
        Generator generator = new Generator();
        
        this.number = generator.generateUniqueCreditCard();
        this.account = account;
        this.expirationDate = LocalDate.now().plusMonths(41);
        this.CVV = Generator.generateCVV();
    }
    
    /**
     * Constructor para descargar una tarjeta.
     *
     * @param number         Número de la tarjeta.
     * @param account        Cuenta asociada a la tarjeta.
     * @param expirationDate Fecha de caducidad de la tarjeta.
     * @param CVV            CVV de la tarjeta.
     */
    public CreditCard(String number, Account account, LocalDate expirationDate, String CVV) {
        this.number = number;
        this.account = account;
        this.expirationDate = expirationDate;
        this.CVV = CVV;
    }
    
    
    //Getters
    /**
     * Devuelve el número de la tarjeta.
     *
     * @return Número de la tarjeta.
     */
    public String getNumber() {
        return number;
    }
    
    /**
     * Devuelve la cuenta asociada a la tarjeta.
     *
     * @return Cuenta asociada a la tarjeta.
     */
    public Account getAccount() {
        return account;
    }
    
    /**
     * Devuelve la fecha de caducidad de la tarjeta.
     *
     * @return Fecha de caducidad de la tarjeta.
     */
    public LocalDate getExpirationDate() {
        return expirationDate;
    }
    
    /**
     * Devuelve el CVV de la tarjeta.
     *
     * @return CVV de la tarjeta.
     */
    public String getCVV() {
        return CVV;
    }
    
    
    //Override
    /**
     * Devuelve una cadena con la información de la tarjeta.
     *
     * @return Cadena con la información de la tarjeta.
     */
    @Override
    public String toString() {
        return "Credit Card " + number + ": IBAN: " + account.getIBAN() + " , Expiration Date: " + expirationDate.format(formatter) + " , CVV: " + CVV;
    }
}
