package org.HappyBank.model.repository;

import org.HappyBank.model.Account;
import org.HappyBank.model.Client;
import org.HappyBank.model.CreditCard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.net.URI;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doReturn;

public class CreditCardRepositoryImplTest {
    private CreditCardRepositoryImpl cardRepository;
    private PreparedStatement mockPreparedStatement;
    private Statement mockStatement;
    private Connection mockConnection;
    private ResultSet mockResultSet;
    private CreditCard mockCard;
    private Account mockAccount;
    
    @BeforeClass
    public static void configureLogger() {
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        // Apunta al archivo de configuración en src/test/resources
        context.setConfigLocation(URI.create("src/test/main/resources/log4j2-test.xml"));
    }
    
    @Before
    public void setUp() throws Exception {
        AccountRepositoryImpl mockAccountRepository = mock(AccountRepositoryImpl.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockConnection = mock(Connection.class);
        mockStatement = mock(Statement.class);
        mockResultSet = mock(ResultSet.class);
        
        mockAccount = new Account("ES19 0064 0001 83 9329930006", new Client("Juan", "Pérez", "12345678A", "juanperez@example.com", "123456789", "Calle Falsa 123", "HappyBank"), new BigDecimal(100));
        mockCard = new CreditCard("6440 9643 8090 5200", mockAccount, LocalDate.of(2027, 4, 17), "123");
        
        cardRepository = spy(CreditCardRepositoryImpl.class);
        doReturn(mockConnection).when(cardRepository).getConnection();
        cardRepository.setAccountRepository(mockAccountRepository);
        doReturn(new Account("ES19 0064 0001 83 9329930006", new Client("Juan", "Pérez", "12345678A", "juanperez@example.com", "123456789", "Calle Falsa 123", "HappyBank"), new BigDecimal(100))).when(mockAccountRepository).get(anyString());
    }
    
    
    @Test
    public void getConnectionTest() throws Exception {
        CreditCardRepositoryImpl cardRepositoryTrue = new CreditCardRepositoryImpl();
        Connection connection = cardRepositoryTrue.getConnection();
        assertNotNull(connection);
    }
    
    
    @Test
    public void addTest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        cardRepository.add(mockCard);
        
        verify(mockPreparedStatement).setString(1, "6440 9643 8090 5200");
        verify(mockPreparedStatement).setString(2, "ES19 0064 0001 83 9329930006");
        verify(mockPreparedStatement).setDate(3, Date.valueOf(LocalDate.of(2027, 4, 17)));
        verify(mockPreparedStatement).setString(4, "123");
        verify(mockPreparedStatement).executeQuery();
    }
    
    @Test
    public void addFailTest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Error de conexión"));
        
        try {
            cardRepository.add(mockCard);
            fail("Se esperaba un RuntimeException");
        } catch (RuntimeException e) {
            assertEquals("Error creating or executing the query: Error de conexión", e.getMessage());
        }
    }
    
    
    @Test
    public void removeTest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        cardRepository.remove(mockCard);
        
        verify(mockPreparedStatement).setString(1, "6440 9643 8090 5200");
        verify(mockPreparedStatement).executeQuery();
    }
    
    @Test
    public void removeFailTest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Error de conexión"));
        
        try {
            cardRepository.remove(mockCard);
            fail("Se esperaba un RuntimeException");
        } catch (RuntimeException e) {
            assertEquals("Error creating or executing the query: Error de conexión", e.getMessage());
        }
    }
    
    
    @Test
    public void updateFailTest() {
        try {
            cardRepository.update(mockCard);
            fail("Se esperaba un UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("This method is unsupported.", e.getMessage());
        }
    }
    
    
    @Test
    public void getByNumberExistsTest() throws Exception {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("Number")).thenReturn("6440 9643 8090 5200");
        when(mockResultSet.getString("AccountIBAN")).thenReturn("ES19 0064 0001 83 9329930006");
        when(mockResultSet.getDate("ExpirationDate")).thenReturn(Date.valueOf(LocalDate.of(2027, 4, 17)));
        when(mockResultSet.getString("CVV")).thenReturn("123");
        
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        CreditCard result = cardRepository.get("6440 9643 8090 5200");
        
        assertNotNull(result);
        assertEquals("6440 9643 8090 5200", result.getNumber());
        assertEquals("ES19 0064 0001 83 9329930006", result.getAccount().getIBAN());
        assertEquals("2027-04-17", result.getExpirationDate().toString());
        assertEquals("123", result.getCVV());
    }
    
    @Test
    public void getByNumberDoesNotExistTest() throws Exception {
        when(mockResultSet.next()).thenReturn(false);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        
        try {
            cardRepository.get("6440 9643 8090 5200");
            fail("Se esperaba una RuntimeException porque el administrador no existe");
        } catch (RuntimeException e) {
            assertEquals("The credit card does not exist.", e.getMessage());
        }
    }
    
    @Test
    public void getByNumberFailTest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Error de conexión"));
        
        try {
            cardRepository.get("6440 9643 8090 5200");
            fail("Se esperaba una SQLException");
        } catch (RuntimeException e) {
            assertEquals("Error creating or executing the query: Error de conexión", e.getMessage());
        }
    }
    
    
    @Test
    public void getByAccountExistsTest() throws Exception {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("Number")).thenReturn("6440 9643 8090 5200");
        when(mockResultSet.getString("AccountIBAN")).thenReturn("ES19 0064 0001 83 9329930006");
        when(mockResultSet.getDate("ExpirationDate")).thenReturn(Date.valueOf(LocalDate.of(2027, 4, 17)));
        when(mockResultSet.getString("CVV")).thenReturn("123");
        
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        CreditCard result = cardRepository.get(mockAccount);
        
        assertNotNull(result);
        assertEquals("6440 9643 8090 5200", result.getNumber());
        assertEquals("ES19 0064 0001 83 9329930006", result.getAccount().getIBAN());
        assertEquals("2027-04-17", result.getExpirationDate().toString());
        assertEquals("123", result.getCVV());
    }
    
    @Test
    public void getByAccountDoesNotExistTest() throws Exception {
        when(mockResultSet.next()).thenReturn(false);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        
        try {
            cardRepository.get(mockAccount);
            fail("Se esperaba una RuntimeException porque el administrador no existe");
        } catch (RuntimeException e) {
            assertEquals("The credit card does not exist.", e.getMessage());
        }
    }
    
    @Test
    public void getByAccountFailTest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Error de conexión"));
        
        try {
            cardRepository.get(mockAccount);
            fail("Se esperaba una SQLException");
        } catch (RuntimeException e) {
            assertEquals("Error creating or executing the query: Error de conexión", e.getMessage());
        }
    }
    
    
    @Test
    public void getAllExistsTest() throws Exception {
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("Number")).thenReturn("6440 9643 8090 5200");
        when(mockResultSet.getString("AccountIBAN")).thenReturn("ES19 0064 0001 83 9329930006");
        when(mockResultSet.getDate("ExpirationDate")).thenReturn(Date.valueOf(LocalDate.of(2027, 4, 17)));
        when(mockResultSet.getString("CVV")).thenReturn("123");
        
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        
        ArrayList<CreditCard> result = cardRepository.getAll();
        
        assertNotNull(result);
        assertEquals("6440 9643 8090 5200", result.getFirst().getNumber());
        assertEquals("ES19 0064 0001 83 9329930006", result.getFirst().getAccount().getIBAN());
        assertEquals("2027-04-17", result.getFirst().getExpirationDate().toString());
        assertEquals("123", result.getFirst().getCVV());
    }
    
    @Test
    public void testGetAllNotExists() throws Exception {
        when(mockResultSet.next()).thenReturn(false);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        
        try {
            cardRepository.getAll();
            fail("Se esperaba una RuntimeException porque la tarjeta de crédito no existe");
        } catch (RuntimeException e) {
            assertEquals("There are no credit cards.", e.getMessage());
        }
    }
    
    @Test
    public void getAllFailTest() throws Exception {
        when(mockConnection.createStatement()).thenThrow(new SQLException("Error de conexión"));
        
        try {
            cardRepository.getAll();
            fail("Se esperaba una SQLException");
        } catch (RuntimeException e) {
            assertEquals("Error creating or executing the query: Error de conexión", e.getMessage());
        }
    }
    
    
    @Test
    public void testIsIBAN() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        
        boolean exists = cardRepository.isNumber("6440 9643 8090 5200");
        
        assertTrue(exists);
        verify(mockPreparedStatement).setString(1, "6440 9643 8090 5200");
        verify(mockPreparedStatement).executeQuery();
    }
    
    @Test
    public void isIBANFailTest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Error de conexión"));
        
        try {
            cardRepository.isNumber("6440 9643 8090 5200");
            fail("Se esperaba una SQLException");
        } catch (RuntimeException e) {
            assertEquals("Error creating or executing the query: Error de conexión", e.getMessage());
        }
    }
}


