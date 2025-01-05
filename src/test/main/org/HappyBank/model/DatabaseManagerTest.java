package org.HappyBank.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.HappyBank.model.DatabaseManager.*;

public class DatabaseManagerTest {
    Client c;
    Administrator a;
    ArrayList<Transaction> transactions;
    
    
    //Before
    @Before
    public void setUp() throws Exception {
        getInstance();
        
        c = new Client("Juan", "Pérez", "12345678A", "juanperez@gmail.com", "123456789", "Calle de Prueba 123", "HappyBank");
        a = new Administrator("Juan", "Pérez", "12345678A", "400123001", new BigDecimal(2000), "HappyBank");
        
        Client ct = new Client("María", "Gómez", "67854321A", "mariagomez@gmail.com", "678954321", "Calle de Prueba 321", "HappyBank");
        
        Account[] accounts = {
                new Account("ES1234567890123456789012", "12345678A", new BigDecimal(1000)),
                new Account("ES1234567890987654321098", "87654321Z", new BigDecimal(3000))
        };
        
        transactions = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            transactions.add( new Transaction(accounts[0], accounts[1], "Concepto de prueba" , new BigDecimal(100), LocalDateTime.of(2021, i+1, 1, 12, 0, 0)));
        }
        
        insertClient(c, "1234");
        insertClient(ct, "1234");
        insertAdministrator(a, "1234");
        insertAccount(accounts[0]);
        insertAccount(accounts[1]);
        for(Transaction t : transactions) {
            insertTransaction(t);
        }
    }
    
    
    //Client
    @Test
    public void insertClientTest() throws HappyBankException {
        deleteClient("12345678A");
        insertClient(c, "1234");
    }
    
    @Test
    public void getExistentClientTest() throws HappyBankException {
        assertEquals(c.toString(), getClient("12345678A").toString());
    }
    
    @Test
    public void correctLoginClientTest() throws HappyBankException {
        assertTrue(loginClient("12345678A", "1234"));
    }
    
    @Test
    public void incorrectLoginClientTest() throws HappyBankException {
        assertFalse(loginClient("12345678A", "12345"));
    }
    
    @Test
    public void deleteClientTest() throws HappyBankException {
        deleteClient("12345678A");
    }
    
    @Test (expected = HappyBankException.class)
    public void getNullClientTest() throws HappyBankException {
        deleteClient("12345678A");
        assertEquals(c.toString(), getClient("12345678A").toString());
    }
    
    @Test
    public void nullClientLoginTest() throws HappyBankException {
        deleteClient("12345678A");
        assertFalse(loginClient("12345678A", "1234"));
    }
    
    
    //Administrator
    @Test
    public void insertAdministratorTest() throws HappyBankException {
        deleteAdministrator("12345678A");
        insertAdministrator(a, "1234");
    }
    
    @Test
    public void getExistentAdministratorTest() throws HappyBankException {
        assertEquals(a.toString(), getAdministrator("12345678A").toString());
    }
    
    @Test
    public void correctLoginAdministratorTest() throws HappyBankException {
        assertTrue(loginAdministrator("12345678A", "1234"));
    }
    
    @Test
    public void incorrectLoginAdministratorTest() throws HappyBankException {
        assertFalse(loginAdministrator("12345678A", "12345"));
    }
    
    @Test
    public void deleteAdministratorTest() throws HappyBankException {
        deleteAdministrator("12345678A");
    }
    
    @Test (expected = HappyBankException.class)
    public void getNullAdministratorTest() throws HappyBankException {
        deleteAdministrator("12345678A");
        assertEquals(a.toString(), getAdministrator("12345678A").toString());
    }
    
    @Test
    public void nullAdministratorLoginTest() throws HappyBankException {
        deleteAdministrator("12345678A");
        assertFalse(loginAdministrator("12345678A", "1234"));
    }
    
    
    //Account
    @Test
    public void insertAccountTest() throws HappyBankException {
        deleteAccount("ES1234567890123456789012");
        insertAccount(new Account("ES1234567890123456789012", "12345678A", new BigDecimal(1000)));
    }
    
    @Test
    public void getExistentAccountTest() throws HappyBankException {
        assertEquals("ES1234567890123456789012", getAccount("ES1234567890123456789012").getIBAN());
    }
    
    @Test (expected = HappyBankException.class)
    public void getNotExistentAccountTest() throws HappyBankException {
        getAccount("ES1234567890123456789013");
    }
    
    @Test
    public void deleteAccountTest() throws HappyBankException {
        deleteAccount("ES1234567890123456789012");
    }
    
    
    //Transaction
    @Test
    public void insertTransactionTest() throws HappyBankException {
        deleteTransaction(transactions.getFirst().getID());
        insertTransaction(transactions.getFirst());
    }
    
    @Test
    public void getExistentTransactionTest() throws HappyBankException {
        assertEquals(transactions.getFirst().toString(), getTransaction(transactions.getFirst().getID()).toString());
    }
    
    @Test
    public void getLastTransactionsTest() throws HappyBankException {
        Comparator<Transaction> comparator = (t1, t2) -> t2.getDate().compareTo(t1.getDate());
        ArrayList<Transaction> database = getLastTransactions();
        
        transactions.sort(comparator);
        for (int i = 0; i < database.size(); i++) {
            assertEquals(transactions.get(i).toString(), database.get(i).toString());
        }
        
    }
    
    @Test
    public void getAccountTransactionTest() throws HappyBankException {
        ArrayList<Transaction> database = getAccountTransactions("ES1234567890123456789012");
        for (int i = 0; i < 10; i++) {
            assertEquals(transactions.get(i).toString(), database.get(i).toString());
        }
    }
    
    @Test
    public void deleteTransactionTest() throws HappyBankException {
        deleteTransaction(transactions.getFirst().getID());
    }
    
    @Test (expected = HappyBankException.class)
    public void getNullTransactionTest() throws HappyBankException {
        deleteTransaction(transactions.getFirst().getID());
        assertEquals(transactions.getFirst().toString(), getTransaction(transactions.getFirst().getID()).toString());
    }
    
    
    //After
    @After
    public void tearDown() throws Exception {
        for (int i = 0; i < 10; i++) {
            deleteTransaction(transactions.get(i).getID());
        }
        
        deleteAccount("ES1234567890123456789012");
        deleteAccount("ES1234567890987654321098");
        
        deleteClient("12345678A");
        deleteClient("67854321A");
        deleteAdministrator("12345678A");
    }
}
