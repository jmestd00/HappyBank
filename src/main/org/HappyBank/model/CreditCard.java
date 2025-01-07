package org.HappyBank.model;

import org.HappyBank.model.repository.CreditCardRepositoryImpl;

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
     * IBAN de la cuenta asociada a la tarjeta.
     */
    private final String IBAN;
    /**
     * Fecha de caducidad de la tarjeta.
     */
    private final LocalDate expirationDate;
    /**
     * CVV de la tarjeta.
     */
    private final String CVV;
    /**
     * Conexión con la base de datos
     */
    private final CreditCardRepositoryImpl cardRepository;
    /**
     * Formateador de fechas en día/mes/año.
     */
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    
    //Constructors
    /**
     * Constructor con parámetros.
     *
     * @param number         Número de la tarjeta.
     * @param IBAN           IBAN de la tarjeta.
     * @param expirationDate Fecha de caducidad de la tarjeta.
     * @param CVV            CVV de la tarjeta.
     */
    public CreditCard(String number, String IBAN, LocalDate expirationDate, String CVV) {
        cardRepository = new CreditCardRepositoryImpl();
        this.number = number;
        this.IBAN = IBAN;
        this.expirationDate = expirationDate;
        this.CVV = CVV;
        
        cardRepository.add(this);
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
     * Devuelve el IBAN de la tarjeta.
     *
     * @return IBAN de la tarjeta.
     */
    public String getIBAN() {
        return IBAN;
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
        return "Credit Card " + number + ": IBAN: " + IBAN + " , Expiration Date: " + expirationDate.format(formatter) + " , CVV: " + CVV;
    }
}
