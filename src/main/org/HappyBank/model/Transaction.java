package org.HappyBank.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    //Attributes
    /**
     * Identificador de la transacción.
     */
    private final int ID;
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
     * Contador de identificadores.
     */
    private static int IDCounter = 1;
    /**
     * Formateador de tiempo día/mes/año hora:minuto:segundo.
     */
    private static final DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    
    //Constructors
    /**
     * Constructor para la DB.
     * @param ID Identificador de la transacción.
     * @param sender Cuenta emisora.
     * @param receiver Cuenta receptora.
     * @param concept Concepto de la transacción
     * @param amount Cantidad de dinero.
     * @param date Fecha de la transacción.
     */
    public Transaction(int ID, Account sender, Account receiver, String concept, BigDecimal amount, LocalDateTime date) {
        this.ID = ID;
        this.sender = sender;
        this.receiver = receiver;
        this.concept = concept;
        this.amount = amount;
        this.date = date;
    }
    
    /**
     * Constructor con parámetros.
     * @param sender Cuenta emisora.
     * @param receiver Cuenta receptora.
     * @param concept Concepto de la transacción
     * @param amount Cantidad de dinero.
     * @param date Fecha de la transacción.
     */
    public Transaction(Account sender, Account receiver, String concept, BigDecimal amount, LocalDateTime date) throws HappyBankException {
        if (receiver.equals(sender)) {
            throw new HappyBankException("The sender and the receiver can't be the same account");
        }
        
        this.ID = IDCounter;
        IDCounter++;
        this.sender = sender;
        this.receiver = receiver;
        this.concept = concept;
        this.amount = amount;
        this.date = date;
    }
    
    /**
     * Constructor con parámetros.
     * @param sender Cuenta emisora.
     * @param receiver Cuenta receptora.
     * @param concept Concepto de la transacción
     * @param amount Cantidad de dinero.
     */
    public Transaction(Account sender, Account receiver, String concept, BigDecimal amount) throws HappyBankException {
        if (receiver.equals(sender)) {
            throw new HappyBankException("The sender and the receiver can't be the same account");
        }
        
        this.ID = IDCounter;
        IDCounter++;
        this.sender = sender;
        this.receiver = receiver;
        this.concept = concept;
        this.amount = amount;
        this.date = LocalDateTime.now();
    }
    
    
    //Getters
    /**
     * Devuelve el identificador de la transacción.
     * @return Identificador de la transacción.
     */
    public int getID() {
        return ID;
    }
    
    /**
     * Devuelve la cuenta emisora.
     * @return Cuenta emisora.
     */
    public Account getSender() {
        return sender;
    }
    
    /**
     * Devuelve la cuenta receptora.
     * @return Cuenta receptora.
     */
    public Account getReceiver() {
        return receiver;
    }
    
    /**
     * Devuelve el concepto de la transacción.
     * @return Concepto de la transacción.
     */
    public String getConcept() {
        return concept;
    }
    
    /**
     * Devuelve la cantidad de dinero.
     * @return Cantidad de dinero.
     */
    public BigDecimal getAmount() {
        return amount;
    }
    
    /**
     * Devuelve la fecha de la transacción.
     * @return Fecha de la transacción.
     */
    public LocalDateTime getDate() {
        return date;
    }
    
    
    //Override
    /**
     * Devuelve una cadena con la información de la transacción.
     * @return Cadena con la información de la transacción.
     */
    @Override
    public String toString() {
        return "Transaction " + ID + ": \nDate: " + date.format(formater) + ", \nAmount: " + amount.setScale(2, RoundingMode.HALF_UP) + ", \nConcept: " + concept + ", \nSender Client: " + sender.toString() + ", \nReceiver Client: " + receiver.toString();
    }
}
