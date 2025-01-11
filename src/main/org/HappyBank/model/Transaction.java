package org.HappyBank.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase que representa una transacción.
 */
public class Transaction {
    //Attributes
    /**
     * Cuenta emisora.
     */
    private final Account sender;
    /**
     * Cuenta receptora.
     */
    private final Account receiver;
    /**
     * Concepto de la transacción.
     */
    private final String concept;
    /**
     * Cantidad transferida.
     */
    private final BigDecimal amount;
    /**
     * Fecha de la transacción.
     */
    private final LocalDateTime date;
    /**
     * Formateador de tiempo día/mes/año hora:minuto:segundo.
     */
    private static final DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    
    //Constructors
    /**
     * Constructor para crear una transferencia.
     *
     * @param sender   Cuenta emisora.
     * @param receiver Cuenta receptora.
     * @param concept  Concepto de la transacción
     * @param amount   Cantidad de dinero.
     * @throws RuntimeException Si el emisor y el receptor son la misma cuenta.
     */
    public Transaction(Account sender, Account receiver, String concept, BigDecimal amount) throws HappyBankException {
        if (receiver.equals(sender)) {
            throw new HappyBankException("The sender and the receiver can't be the same account");
        }
        
        this.sender = sender;
        this.receiver = receiver;
        this.concept = concept;
        this.amount = amount;
        this.date = LocalDateTime.now();
    }
    
    /**
     * Constructor para descargar una transferencia.
     *
     * @param sender   Cuenta emisora.
     * @param receiver Cuenta receptora.
     * @param concept  Concepto de la transacción
     * @param amount   Cantidad de dinero.
     * @param date     Fecha de la transacción.
     */
    public Transaction(Account sender, Account receiver, String concept, BigDecimal amount, LocalDateTime date) {
        this.sender = sender;
        this.receiver = receiver;
        this.concept = concept;
        this.amount = amount;
        this.date = date;
    }
    
    
    //Getters
    /**
     * Devuelve la cuenta emisora.
     *
     * @return Cuenta emisora.
     */
    public Account getSender() {
        return sender;
    }
    
    /**
     * Devuelve la cuenta receptora.
     *
     * @return Cuenta receptora.
     */
    public Account getReceiver() {
        return receiver;
    }
    
    /**
     * Devuelve el concepto de la transacción.
     *
     * @return Concepto de la transacción.
     */
    public String getConcept() {
        return concept;
    }
    
    /**
     * Devuelve la cantidad de dinero.
     *
     * @return Cantidad de dinero.
     */
    public BigDecimal getAmount() {
        return amount;
    }
    
    /**
     * Devuelve la fecha de la transacción.
     *
     * @return Fecha de la transacción.
     */
    public LocalDateTime getDate() {
        return date;
    }
    
    
    //Override
    /**
     * Devuelve una cadena con la información de la transacción.
     *
     * @return Cadena con la información de la transacción.
     */
    @Override
    public String toString() {
        return "Transaction: \nDate: " + date.format(formater) + ", \nAmount: " + amount.setScale(2, RoundingMode.HALF_UP) + ", \nConcept: " + concept + ", \nSender Client: " + sender.toString() + ", \nReceiver Client: " + receiver.toString();
    }
}
