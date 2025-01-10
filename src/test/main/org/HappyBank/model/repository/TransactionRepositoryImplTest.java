package org.HappyBank.model.repository;

import org.HappyBank.model.Account;
import org.HappyBank.model.Client;
import org.HappyBank.model.Transaction;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doReturn;

public class TransactionRepositoryImplTest {
    private TransactionRepositoryImpl transactionRepository;
    private PreparedStatement mockPreparedStatement;
    private Connection mockConnection;
    private ResultSet mockResultSet;
    private Transaction mockTransaction;
    private Account mockSender;
    
    @Before
    public void setUp() throws Exception {
        AccountRepositoryImpl mockAccountRepository = mock(AccountRepositoryImpl.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockConnection = mock(Connection.class);
        mockResultSet = mock(ResultSet.class);
        
        mockSender = new Account("ES19 0064 0001 83 9329930006", new Client("Juan", "Pérez", "12345678A", "juanperez@example.com", "123456789", "Calle Falsa 123", "HappyBank"), new BigDecimal(100));
        Account mockReceiver = new Account("ES07 0064 0001 60 9168908623", new Client("Pedro", "García", "87654321Z", "pedrogarcia@example.com", "987654321", "Calle Irreal 321", "HappyBank"), new BigDecimal(100));
        mockTransaction = new Transaction(mockSender, mockReceiver, "Concepto", new BigDecimal(100), LocalDateTime.of(2025, 1, 1, 12, 0));
        
        transactionRepository = spy(TransactionRepositoryImpl.class);
        doReturn(mockConnection).when(transactionRepository).getConnection();
        transactionRepository.setAccountRepository(mockAccountRepository);
        doReturn(mockSender).when(mockAccountRepository).get("ES19 0064 0001 83 9329930006");
        doReturn(mockReceiver).when(mockAccountRepository).get("ES07 0064 0001 60 9168908623");
    }
    
    
    @Test
    public void getConnectionTest() throws Exception {
        TransactionRepositoryImpl transactionRepositoryTrue = new TransactionRepositoryImpl();
        Connection connection = transactionRepositoryTrue.getConnection();
        assertNotNull(connection);
    }
    
    
    @Test
    public void addTest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        transactionRepository.add(mockTransaction);
        
        verify(mockPreparedStatement).setString(1, "Concepto");
        verify(mockPreparedStatement).setString(2, "ES19 0064 0001 83 9329930006");
        verify(mockPreparedStatement).setString(3, "ES07 0064 0001 60 9168908623");
        verify(mockPreparedStatement).setBigDecimal(4, new BigDecimal(100));
        verify(mockPreparedStatement).setTimestamp(5, Timestamp.valueOf(LocalDateTime.of(2025, 1, 1, 12, 0)));
        verify(mockPreparedStatement).executeQuery();
    }
    
    @Test
    public void addFailTest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Error de conexión"));
        
        try {
            transactionRepository.add(mockTransaction);
            fail("Se esperaba un RuntimeException");
        } catch (RuntimeException e) {
            assertEquals("Error creating or executing the query: Error de conexión", e.getMessage());
        }
    }
    
    
    @Test
    public void removeTest() {
        try {
            transactionRepository.remove(mockTransaction);
            fail("Se esperaba un UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("This method is unsupported.", e.getMessage());
        }
    }
    
    
    @Test
    public void updateTest() {
        try {
            transactionRepository.update(mockTransaction);
            fail("Se esperaba un UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("This method is unsupported.", e.getMessage());
        }
    }
    
    
    @Test
    public void getTest() {
        try {
            transactionRepository.get("");
            fail("Se esperaba un UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("This method is unsupported.", e.getMessage());
        }
    }
    
    @Test
    public void getAllTest() {
        try {
            transactionRepository.getAll();
            fail("Se esperaba un UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("This method is unsupported.", e.getMessage());
        }
    }
    
    @Test
    public void getAllExistsTest() throws Exception {
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("Concept")).thenReturn("Concepto");
        when(mockResultSet.getString("Sender")).thenReturn("ES19 0064 0001 83 9329930006");
        when(mockResultSet.getString("Receiver")).thenReturn("ES07 0064 0001 60 9168908623");
        when(mockResultSet.getBigDecimal("Amount")).thenReturn(new BigDecimal(100));
        when(mockResultSet.getTimestamp("Date")).thenReturn(Timestamp.valueOf(LocalDateTime.of(2025, 1, 1, 12, 0)));
        
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        
        ArrayList<Transaction> result = transactionRepository.getAccountTransactions(mockSender);
        
        assertNotNull(result);
        assertEquals("Concepto", result.getFirst().getConcept());
        assertEquals("ES19 0064 0001 83 9329930006", result.getFirst().getSender().getIBAN());
        assertEquals("ES07 0064 0001 60 9168908623", result.getFirst().getReceiver().getIBAN());
        assertEquals(new BigDecimal(100), result.getFirst().getAmount());
        assertEquals("2025-01-01T12:00", result.getFirst().getDate().toString());
    }
    
    @Test
    public void getAllDoesNotExistTest() throws Exception {
        when(mockResultSet.next()).thenReturn(false);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        
        try {
            transactionRepository.getAccountTransactions(mockSender);
            fail("Se esperaba una RuntimeException porque la transacción no existe");
        } catch (RuntimeException e) {
            assertEquals("There are no transactions.", e.getMessage());
        }
    }
    
    @Test
    public void getAllFailTest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Error de conexión"));
        
        try {
            transactionRepository.getAccountTransactions(mockSender);
            fail("Se esperaba una SQLException");
        } catch (RuntimeException e) {
            assertEquals("Error creating or executing the query: Error de conexión", e.getMessage());
        }
    }
    
    @Test
    public void getLastExistsMoreTest() throws Exception {
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getString("Concept")).thenReturn("Concepto");
        when(mockResultSet.getString("Sender")).thenReturn("ES19 0064 0001 83 9329930006");
        when(mockResultSet.getString("Receiver")).thenReturn("ES07 0064 0001 60 9168908623");
        when(mockResultSet.getBigDecimal("Amount")).thenReturn(new BigDecimal(100));
        when(mockResultSet.getTimestamp("Date")).thenReturn(Timestamp.valueOf(LocalDateTime.of(2025, 1, 1, 12, 0)));
        
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        
        ArrayList<Transaction> result = transactionRepository.getLastTransactions(mockSender, 3);
        
        assertNotNull(result);
        assertEquals("Concepto", result.getFirst().getConcept());
        assertEquals("ES19 0064 0001 83 9329930006", result.getFirst().getSender().getIBAN());
        assertEquals("ES07 0064 0001 60 9168908623", result.getFirst().getReceiver().getIBAN());
        assertEquals(new BigDecimal(100), result.getFirst().getAmount());
        assertEquals("2025-01-01T12:00", result.getFirst().getDate().toString());
    }
    
    @Test
    public void getLastExistsTest() throws Exception {
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getString("Concept")).thenReturn("Concepto");
        when(mockResultSet.getString("Sender")).thenReturn("ES19 0064 0001 83 9329930006");
        when(mockResultSet.getString("Receiver")).thenReturn("ES07 0064 0001 60 9168908623");
        when(mockResultSet.getBigDecimal("Amount")).thenReturn(new BigDecimal(100));
        when(mockResultSet.getTimestamp("Date")).thenReturn(Timestamp.valueOf(LocalDateTime.of(2025, 1, 1, 12, 0)));
        
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        
        ArrayList<Transaction> result = transactionRepository.getLastTransactions(mockSender, 1);
        
        assertNotNull(result);
        assertEquals("Concepto", result.getFirst().getConcept());
        assertEquals("ES19 0064 0001 83 9329930006", result.getFirst().getSender().getIBAN());
        assertEquals("ES07 0064 0001 60 9168908623", result.getFirst().getReceiver().getIBAN());
        assertEquals(new BigDecimal(100), result.getFirst().getAmount());
        assertEquals("2025-01-01T12:00", result.getFirst().getDate().toString());
    }
}
