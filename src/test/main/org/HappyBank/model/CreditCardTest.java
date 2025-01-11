package org.HappyBank.model;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CreditCardTest {
    CreditCard mockCard;
    Account mockAccount;
    
    @Before
    public void setUp() {
        mockAccount = new Account("ES19 0064 0001 83 9329930006", new Client("Juan", "Pérez", "12345678A", "juanperez@example.com", "123456789", "Calle Falsa 123", "HappyBank"), new BigDecimal(100));
        mockCard = new CreditCard("6440 9643 8090 5200", mockAccount, LocalDate.of(2027, 4, 17), "123");
    }
    
    @Test
    public void createTest() {
        CreditCard cardCreated = new CreditCard(mockAccount);
        CreditCard cardGotten = new CreditCard("6440 9643 8090 5200", mockAccount, LocalDate.of(2027, 4, 17), "123");
        
        assertNotNull(cardCreated);
        assertNotNull(cardGotten);
    }
    
    @Test
    public void getNumberTest() {
        assertEquals("6440 9643 8090 5200", mockCard.getNumber());
    }
    
    @Test
    public void getIBANTest() {
        assertEquals("ES19 0064 0001 83 9329930006", mockCard.getAccount().getIBAN());
    }
    
    @Test
    public void getExpirationDateTest() {
        assertEquals(LocalDate.of(2027, 4, 17), mockCard.getExpirationDate());
    }
    
    @Test
    public void getCVVTest() {
        assertEquals("123", mockCard.getCVV());
    }
    
    @Test
    public void toStringTest() {
        assertEquals("Credit Card 6440 9643 8090 5200: IBAN: ES19 0064 0001 83 9329930006 , Expiration Date: 04/27 , CVV: 123", mockCard.toString());
    }
}
