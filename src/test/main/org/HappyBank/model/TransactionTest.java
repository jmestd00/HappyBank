package org.HappyBank.model;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class TransactionTest {
    Account account1;
    Account account2;
    Transaction transaction;
    LocalDateTime date = LocalDateTime.of(2024, 12, 25, 12, 30, 10);
    
    @Before
    public void setUp() throws HappyBankException {
        account1 = new Account("ES6621000418401234567891", "12345678A", new BigDecimal(1000));
        account2 = new Account("ES6621000418401234567892", "12345678B", new BigDecimal(1000));
        transaction = new Transaction(0, account1, account2, "Concepto de la transacción", new BigDecimal(100), date);
        Transaction transactionTest1 = new Transaction(account1, account2, "Concepto de la transacción", new BigDecimal(100));
        Transaction transactionTest2 = new Transaction(account1, account2, "Concepto de la transacción", new BigDecimal(100), date);
    }
    
    @Test
    public void getSenderTest() {
        assertEquals("ES6621000418401234567891", transaction.getSender().getIBAN());
    }
    
    @Test
    public void getReceiverTest() {
        assertEquals("ES6621000418401234567892", transaction.getReceiver().getIBAN());
    }
    
    @Test
    public void getConceptTest() {
        assertEquals("Concepto de la transacción", transaction.getConcept());
    }
    
    @Test
    public void getAmountTest() {
        assertEquals(new BigDecimal(100), transaction.getAmount());
    }
    
    @Test
    public void getIDTest() {
        assertEquals(0, transaction.getID());
    }
    
    @Test
    public void getDateTest() {
        assertEquals(date, transaction.getDate());
    }
    
    @Test (expected = HappyBankException.class)
    public void createInvalidTransactionWithoutDate() throws HappyBankException {
        Transaction t = new Transaction(account1, account1, "Concepto de la transacción", new BigDecimal(100));
    }
    
    @Test (expected = HappyBankException.class)
    public void createInvalidTransactionWithDate() throws HappyBankException {
        Transaction t = new Transaction(account1, account1, "Concepto de la transacción", new BigDecimal(100), date);
    }
    
    @Test
    public void toStringTest() {
        assertEquals("""
                Transaction 0:\s
                Date: 25/12/2024 12:30:10,\s
                Amount: 100.00,\s
                Concept: Concepto de la transacción,\s
                Sender Client: Account ES6621000418401234567891 , Owner NIF: 12345678A , Balance: 1000.00,\s
                Receiver Client: Account ES6621000418401234567892 , Owner NIF: 12345678B , Balance: 1000.00""",
                
                transaction.toString());
    }
}
