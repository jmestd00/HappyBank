package org.HappyBank.model.repository;

import org.HappyBank.model.Account;
import org.HappyBank.model.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.net.URI;
import java.sql.*;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AccountRepositoryImplTest {
    private AccountRepositoryImpl accountRepository;
    private PreparedStatement mockPreparedStatement;
    private Statement mockStatement;
    private Connection mockConnection;
    private ResultSet mockResultSet;
    private Account mockAccount;
    private Client mockClient;
    
    @BeforeClass
    public static void configureLogger() {
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        // Apunta al archivo de configuración en src/test/resources
        context.setConfigLocation(URI.create("src/test/main/resources/log4j2-test.xml"));
    }
    
    @Before
    public void setUp() throws Exception {
        ClientRepositoryImpl mockClientRepository = mock(ClientRepositoryImpl.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockConnection = mock(Connection.class);
        mockStatement = mock(Statement.class);
        mockResultSet = mock(ResultSet.class);
        
        mockAccount = new Account("ES19 0064 0001 83 9329930006", new Client("Juan", "Pérez", "12345678A", "juanperez@example.com", "123456789", "Calle Falsa 123", "HappyBank"), new BigDecimal(100));
        mockClient = new Client("12345678A", "Juan", "Pérez", "juanperez@example.com", "123456789", "Calle Falsa 123", "HappyBank");
        
        accountRepository = spy(AccountRepositoryImpl.class);
        doReturn(mockConnection).when(accountRepository).getConnection();
        accountRepository.setClientRepository(mockClientRepository);
        doReturn(new Client("Juan", "Pérez", "12345678A", "juanperez@example.com", "123456789", "Calle Falsa 123", "HappyBank")).when(mockClientRepository).get(anyString());
    }
    
    @Test
    public void getConnectionTest() throws Exception {
        AccountRepositoryImpl accountRepositoryTrue = new AccountRepositoryImpl();
        Connection connection = accountRepositoryTrue.getConnection();
        assertNotNull(connection);
    }
    
    @Test
    public void addTest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        accountRepository.add(mockAccount);
        
        verify(mockPreparedStatement).setString(1, "ES19 0064 0001 83 9329930006");
        verify(mockPreparedStatement).setString(2, "12345678A");
        verify(mockPreparedStatement).setBigDecimal(3, new BigDecimal(100));
        verify(mockPreparedStatement).executeQuery();
    }
    
    @Test
    public void addFailTest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Error de conexión"));
        
        try {
            accountRepository.add(mockAccount);
            fail("Se esperaba un RuntimeException");
        } catch (RuntimeException e) {
            assertEquals("Error creating or executing the query: Error de conexión", e.getMessage());
        }
    }
    
    @Test
    public void removeTest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        accountRepository.remove(mockAccount);
        
        verify(mockPreparedStatement).setString(1, "ES19 0064 0001 83 9329930006");
        verify(mockPreparedStatement).executeQuery();
    }
    
    @Test
    public void removeFailTest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Error de conexión"));
        
        try {
            accountRepository.remove(mockAccount);
            fail("Se esperaba una SQLException");
        } catch (RuntimeException e) {
            assertEquals("Error creating or executing the query: Error de conexión", e.getMessage());
        }
    }
    
    @Test
    public void updateTest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        accountRepository.update(mockAccount);
        
        verify(mockPreparedStatement).setBigDecimal(1, new BigDecimal(100));
        verify(mockPreparedStatement).setString(2, "ES19 0064 0001 83 9329930006");
        verify(mockPreparedStatement).executeQuery();
    }
    
    @Test
    public void updateFailTest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Error de conexión"));
        
        try {
            accountRepository.update(mockAccount);
            fail("Se esperaba una SQLException");
        } catch (RuntimeException e) {
            assertEquals("Error creating or executing the query: Error de conexión", e.getMessage());
        }
    }
    
    @Test
    public void getByIBANExistsTest() throws SQLException {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("IBAN")).thenReturn("ES19 0064 0001 83 9329930006");
        when(mockResultSet.getString("OwnerNIF")).thenReturn("12345678A");
        when(mockResultSet.getBigDecimal("Balance")).thenReturn(new BigDecimal(100));
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        
        Account result = accountRepository.get("ES19 0064 0001 83 9329930006");
        
        assertNotNull(result);
        assertEquals("ES19 0064 0001 83 9329930006", result.getIBAN());
        assertEquals("12345678A", result.getOwner().getNIF());
        assertEquals(new BigDecimal(100), result.getBalance());
    }
    
    @Test
    public void getByIBANDoesNotExistTest() throws SQLException {
        when(mockResultSet.next()).thenReturn(false);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        
        try {
            accountRepository.get("ES19 0064 0001 83 9329930006");
            fail("Se esperaba una RuntimeException porque la cuenta no existe");
        } catch (RuntimeException e) {
            assertEquals("The account does not exist.", e.getMessage());
        }
    }
    
    @Test
    public void getByIBANFailTest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Error de conexión"));
        
        try {
            accountRepository.get("ES19 0064 0001 83 9329930006");
            fail("Se esperaba una SQLException");
        } catch (RuntimeException e) {
            assertEquals("Error creating or executing the query: Error de conexión", e.getMessage());
        }
    }
    
    @Test
    public void getByClientExistsTest() throws SQLException {
        Client client = new Client("Juan", "Pérez", "12345678A", "juanperez@example.com", "123456789", "Calle Falsa 123", "HappyBank");
        
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("IBAN")).thenReturn("ES19 0064 0001 83 9329930006");
        when(mockResultSet.getString("OwnerNIF")).thenReturn("12345678A");
        when(mockResultSet.getBigDecimal("Balance")).thenReturn(new BigDecimal(100));
        
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        
        Account result = accountRepository.get(client);
        
        assertNotNull(result);
        assertEquals("ES19 0064 0001 83 9329930006", result.getIBAN());
        assertEquals("12345678A", result.getOwner().getNIF());
        assertEquals(new BigDecimal(100), result.getBalance());
    }
    
    @Test
    public void getByClientDoesNotExistTest() throws SQLException {
        when(mockResultSet.next()).thenReturn(false);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        
        try {
            accountRepository.get(mockClient);
            fail("Se esperaba una RuntimeException porque la cuenta no existe");
        } catch (RuntimeException e) {
            assertEquals("The account does not exist.", e.getMessage());
        }
    }
    
    @Test
    public void getByClientFailTest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Error de conexión"));
        
        try {
            accountRepository.get(mockClient);
            fail("Se esperaba una SQLException");
        } catch (RuntimeException e) {
            assertEquals("Error creating or executing the query: Error de conexión", e.getMessage());
        }
    }
    
    @Test
    public void getAllExistsTest() throws SQLException {
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("IBAN")).thenReturn("ES19 0064 0001 83 9329930006");
        when(mockResultSet.getString("OwnerNIF")).thenReturn("12345678A");
        when(mockResultSet.getBigDecimal("Balance")).thenReturn(new BigDecimal(100));
        
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        
        ArrayList<Account> result = accountRepository.getAll();
        
        assertNotNull(result);
        assertEquals("ES19 0064 0001 83 9329930006", result.getFirst().getIBAN());
        assertEquals("12345678A", result.getFirst().getOwner().getNIF());
        assertEquals(new BigDecimal(100), result.getFirst().getBalance());
    }
    
    @Test
    public void testGetAllNotExists() throws SQLException {
        when(mockResultSet.next()).thenReturn(false);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        
        try {
            accountRepository.getAll();
            fail("Se esperaba una RuntimeException porque la cuenta no existe");
        } catch (RuntimeException e) {
            assertEquals("There are no accounts.", e.getMessage());
        }
    }
    
    @Test
    public void getAllFailTest() throws Exception {
        when(mockConnection.createStatement()).thenThrow(new SQLException("Error de conexión"));
        
        try {
            accountRepository.getAll();
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
        
        boolean exists = accountRepository.isIBAN("ES19 0064 0001 83 9329930006");
        
        assertTrue(exists);
        verify(mockPreparedStatement).setString(1, "ES19 0064 0001 83 9329930006");
        verify(mockPreparedStatement).executeQuery();
    }
    
    @Test
    public void isIBANFailTest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Error de conexión"));
        
        try {
            accountRepository.isIBAN("ES19 0064 0001 83 9329930006");
            fail("Se esperaba una SQLException");
        } catch (RuntimeException e) {
            assertEquals("Error creating or executing the query: Error de conexión", e.getMessage());
        }
    }
}
