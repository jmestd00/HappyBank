package org.HappyBank.model;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class CreditCardTest {
    CreditCard creditCard;
    
    @Before
    public void setUp() {
        creditCard = new CreditCard("1234567890123456", "ES6621000418401234567891", LocalDate.of(2023, 12, 31), 123);
    }
    
    @Test
    public void getNumberTest() {
        assertEquals("1234567890123456", creditCard.getNumber());
    }
    
    @Test
    public void getIBANTest() {
        assertEquals("ES6621000418401234567891", creditCard.getIBAN());
    }
    
    @Test
    public void getExpirationDateTest() {
        assertEquals(LocalDate.of(2023, 12, 31), creditCard.getExpirationDate());
    }
    
    @Test
    public void getCVVTest() {
        assertEquals(123, creditCard.getCVV());
    }
    
    @Test
    public void toStringTest() {
        assertEquals("Credit Card 1234567890123456: IBAN: ES6621000418401234567891 , Expiration Date: 31/12/2023 , CVV: 123", creditCard.toString());
    }
}
