package org.HappyBank.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//Record
public class Transaction{
    //Attributes
    private final int ID;
    private final Account sender;
    private final Account receiver;
    private final String concept;
    private final BigDecimal amount;
    private final LocalDateTime date;
    
    private static int IDCounter = 1;
    private static final DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    
    
    //Constructors
    Transaction(int ID, Account sender, Account receiver, String concept, BigDecimal amount, LocalDateTime date) {
        this.ID = ID;
        this.sender = sender;
        this.receiver = receiver;
        this.concept = concept;
        this.amount = amount;
        this.date = date;
    }
    
    Transaction(Account sender, Account receiver, String concept, BigDecimal amount) {
        if (receiver.equals(sender)) {
            throw new IllegalArgumentException("The sender and the receiver can't be the same account");
        }
        
        this.ID = IDCounter; IDCounter++;
        this.sender = sender;
        this.receiver = receiver;
        this.concept = concept;
        this.amount = amount;
        this.date = LocalDateTime.now();
    }

    
    //Override
    @Override
    public String toString() {
        return "Transaction " + ID + ": \nDate: " + date.format(formater) + ", \nAmount: " + amount.setScale(2, RoundingMode.HALF_UP) + ", \nConcept: "
                + concept + ", \nSender Client: " + sender.toString() + ", \nReceiver Client: " + receiver.toString();
    }
}
