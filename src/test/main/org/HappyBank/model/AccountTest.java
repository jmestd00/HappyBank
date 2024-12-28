package org.HappyBank.model;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class AccountTest {
    Account account;
    
    @Before
    public void setUp() {
        account = new Account("ES6621000418401234567891", "12345678A", new BigDecimal(1000));
    }
    
    @Test
    public void getIBANTest() {
        assertEquals("ES6621000418401234567891", account.getIBAN());
    }
    
    @Test
    public void getOwnerNIFTest() {
        assertEquals("12345678A", account.getOwnerNIF());
    }
    
    @Test
    public void getBalanceTest() {
        assertEquals(new BigDecimal(1000), account.getBalance());
    }
    
    @Test
    public void setBalanceTest() {
        account.setBalance(new BigDecimal(2000));
        assertEquals("2000", account.getBalance().toString());
    }
    
    @Test
    public void equalsTest() {
        Account sameAccount = new Account("ES6621000418401234567891", "12345678A", new BigDecimal(1000));
        Account distinctAccount = new Account("ES6621000418401234567892", "12345678B", new BigDecimal(1000));
        Client client = new Client("Juan", "Pérez", "12345678A", "juanperez@gmail.com", "123456789", "Calle de Prueba 123", "HappyBank");
        
        assertEquals(account, sameAccount);
        assertNotEquals(account, distinctAccount);
        assertNotEquals(account, client);
    }
    
    @Test
    public void toStringTest() {
        assertEquals("Account ES6621000418401234567891 , Owner NIF: 12345678A , Balance: 1000.00", account.toString());
    }
}
