package org.HappyBank.model;

import org.HappyBank.model.repository.AccountRepositoryImpl;
import org.HappyBank.model.repository.CreditCardRepositoryImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class GeneratorTest {
    Generator generator;
    AccountRepositoryImpl accountRepository;
    CreditCardRepositoryImpl cardRepository;
    
    @Before
    public void setUp() throws Exception {
        generator = new Generator();
        accountRepository = mock(AccountRepositoryImpl.class);
        cardRepository = mock(CreditCardRepositoryImpl.class);
        
        generator.setAccountRepository(accountRepository);
        generator.setCardRepository(cardRepository);
        
        doReturn(true, false).when(accountRepository).isIBAN(any());
        doReturn(true, false).when(cardRepository).isNumber(any());
    }
    
    @Test
    public void generateIBANTest() {
        String IBAN = generator.generateUniqueIBAN();
        assertNotNull(IBAN);
    }
    
    @Test
    public void generateNumberTest() {
        String card = generator.generateUniqueCreditCard();
        assertNotNull(card);
    }
    
    @Test
    public void generateCVVTest() {
        String CVV = Generator.generateCVV();
        assertNotNull(CVV);
    }
    
    
}
