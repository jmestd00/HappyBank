package org.HappyBank.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TransactionTest {
    Account mockSender;
    Account mockReceiver;
    Transaction mockTransaction;
    LocalDateTime date = LocalDateTime.of(2024, 12, 25, 12, 30, 10);
    
    @Before
    public void setUp() {
        
        mockSender = new Account("ES19 0064 0001 83 9329930006", new Client("Juan", "Pérez", "12345678A", "juanperez@example.com", "123456789", "Calle Falsa 123", "HappyBank"), new BigDecimal(100));
        mockReceiver = new Account("ES07 0064 0001 60 9168908623", new Client("Pedro", "García", "87654321Z", "pedrogarcia@example.com", "987654321", "Calle Irreal 321", "HappyBank"), new BigDecimal(100));
        mockTransaction = new Transaction(mockSender, mockReceiver, "Concepto de la transacción", new BigDecimal(100), date);
    }
    
    @Test
    public void createTransactionsTest() throws HappyBankException {
        Transaction transactionTest1 = new Transaction(mockSender, mockReceiver, "Concepto de la transacción", new BigDecimal(100));
        Transaction transactionTest2 = new Transaction(mockSender, mockReceiver, "Concepto de la transacción", new BigDecimal(100), date);
        
        assertNotNull(transactionTest1);
        assertNotNull(transactionTest2);
    }
    
    @Test
    public void getSenderTest() {
        assertEquals("ES19 0064 0001 83 9329930006", mockTransaction.getSender().getIBAN());
    }
    
    @Test
    public void getReceiverTest() {
        assertEquals("ES07 0064 0001 60 9168908623", mockTransaction.getReceiver().getIBAN());
    }
    
    @Test
    public void getConceptTest() {
        assertEquals("Concepto de la transacción", mockTransaction.getConcept());
    }
    
    @Test
    public void getAmountTest() {
        assertEquals(new BigDecimal(100), mockTransaction.getAmount());
    }
    
    @Test
    public void getDateTest() {
        assertEquals(date, mockTransaction.getDate());
    }
    
    @Test (expected = HappyBankException.class)
    public void createInvalidTransaction() throws HappyBankException {
        Transaction t = new Transaction(mockSender, mockSender, "Concepto", new BigDecimal(100));
        assertNull(t);
    }
    
    @Test
    public void toStringTest() {
        assertEquals("""
                Transaction:\s
                Date: 25/12/2024 12:30:10,\s
                Amount: 100.00,\s
                Concept: Concepto de la transacción,\s
                Sender Client: Account ES19 0064 0001 83 9329930006 , Owner NIF: 12345678A , Balance: 100.00,\s
                Receiver Client: Account ES07 0064 0001 60 9168908623 , Owner NIF: 87654321Z , Balance: 100.00""",
                
                mockTransaction.toString());
    }
}
