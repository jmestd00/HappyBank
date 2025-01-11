package org.HappyBank.model;

import java.math.BigDecimal;

import org.HappyBank.model.repository.AccountRepositoryImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AccountTest {
    AccountRepositoryImpl accountRepository;
    Account mockAccount;
    Client mockClient;
    
    @Before
    public void setUp() {
        accountRepository = mock(AccountRepositoryImpl.class);
        mockClient = new Client("Juan", "Pérez", "12345678A", "juanperez@gmail.com", "123456789", "Calle de Prueba 123", "HappyBank");
        mockAccount = new Account("ES19 0064 0001 83 9329930006", mockClient, new BigDecimal(1000));
        mockAccount.setAccountRepository(accountRepository);
        
        doNothing().when(accountRepository).update(any());
    }
    
    @Test
    public void createTest() {
        Account accountCreated = new Account(mockClient);
        Account accountGotten = new Account("ES19 0064 0001 83 9329930006", mockClient, new BigDecimal(100));
        
        assertNotNull(accountCreated);
        assertNotNull(accountGotten);
    }
    
    @Test
    public void getIBANTest() {
        assertEquals("ES19 0064 0001 83 9329930006", mockAccount.getIBAN());
    }
    
    @Test
    public void getOwnerNIFTest() {
        assertEquals("12345678A", mockAccount.getOwner().getNIF());
    }
    
    @Test
    public void getBalanceTest() {
        assertEquals(new BigDecimal(1000), mockAccount.getBalance());
    }
    
    @Test
    public void setBalanceTest() {
        mockAccount.setBalance(new BigDecimal(2000));
        assertEquals("2000", mockAccount.getBalance().toString());
    }
    
    @Test
    @SuppressWarnings("AssertBetweenInconvertibleTypes")
    public void equalsTest() {
        Account sameAccount = mockAccount;
        Account distinctAccount = new Account("ES07 0064 0001 60 9168908623", new Client("Pedro", "García", "87654321Z", "pedrogarcia@example.com", "987654321", "Calle Irreal 321", "HappyBank"), new BigDecimal(100));
        
        assertEquals(mockAccount, sameAccount);
        assertNotEquals(mockAccount, distinctAccount);
        assertNotEquals(mockAccount, mockAccount.getOwner());
    }
    
    @Test
    public void toStringTest() {
        assertEquals("Account ES19 0064 0001 83 9329930006 , Owner NIF: 12345678A , Balance: 1000.00", mockAccount.toString());
    }
}
