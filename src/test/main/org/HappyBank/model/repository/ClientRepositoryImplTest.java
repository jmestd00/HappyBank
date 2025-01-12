package org.HappyBank.model.repository;

import org.HappyBank.model.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URI;
import java.sql.*;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ClientRepositoryImplTest {
    private ClientRepositoryImpl clientRepository;
    private PreparedStatement mockPreparedStatement;
    private Statement mockStatement;
    private Connection mockConnection;
    private ResultSet mockResultSet;
    private Client mockClient;
    
    @BeforeClass
    public static void configureLogger() {
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        // Apunta al archivo de configuración en src/test/resources
        context.setConfigLocation(URI.create("src/test/main/resources/log4j2-test.xml"));
    }
    
    @Before
    public void setUp() throws Exception {
        mockPreparedStatement = mock(PreparedStatement.class);
        mockConnection = mock(Connection.class);
        mockStatement = mock(Statement.class);
        mockResultSet = mock(ResultSet.class);
        
        clientRepository = spy(ClientRepositoryImpl.class);
        doReturn(mockConnection).when(clientRepository).getConnection();
        
        mockClient = new Client("Juan", "Pérez", "12345678A", "juanperez@example.com", "123456789", "Calle Falsa 123", "HappyBank");
    }
    
    
    @Test
    public void getConnectionTest() throws Exception {
        ClientRepositoryImpl clientRepositoryTrue = new ClientRepositoryImpl();
        Connection connection = clientRepositoryTrue.getConnection();
        assertNotNull(connection);
    }
    
    
    @Test
    public void validateClientTest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        
        boolean exists = clientRepository.validateClient("12345678A", "Password");
        
        assertTrue(exists);
        verify(mockPreparedStatement).setString(1, "12345678A");
        verify(mockPreparedStatement).setString(2, "Password");
        verify(mockPreparedStatement).executeQuery();
    }
    
    @Test
    public void validateClientFailTest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Error de conexión"));
        
        try {
            clientRepository.validateClient("12345678A", "Password");
            fail("Se esperaba un RuntimeException");
        } catch (RuntimeException e) {
            assertEquals("Error creating or executing the query: Error de conexión", e.getMessage());
        }
    }
    
    
    @Test
    public void changePasswordTest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        clientRepository.changePassword("12345678A", "Password2");
        
        verify(mockPreparedStatement).setString(1, "Password2");
        verify(mockPreparedStatement).setString(2, "12345678A");
        verify(mockPreparedStatement).executeQuery();
    }
    
    @Test
    public void changePasswordFailTest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Error de conexión"));
        
        try {
            clientRepository.changePassword("12345678A", "Password2");
            fail("Se esperaba un RuntimeException");
        } catch (RuntimeException e) {
            assertEquals("Error creating or executing the query: Error de conexión", e.getMessage());
        }
    }
    
    
    @Test
    public void addTest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        clientRepository.add(mockClient);
        
        verify(mockPreparedStatement).setString(1, "12345678A");
        verify(mockPreparedStatement).setString(2, "Juan");
        verify(mockPreparedStatement).setString(3, "Pérez");
        verify(mockPreparedStatement).setString(4, "juanperez@example.com");
        verify(mockPreparedStatement).setString(5, "123456789");
        verify(mockPreparedStatement).setString(6, "Calle Falsa 123");
        verify(mockPreparedStatement).setString(7, "HappyBank");
        verify(mockPreparedStatement).setString(8, "Password");
        verify(mockPreparedStatement).executeQuery();
    }
    
    @Test
    public void addFailTest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Error de conexión"));
        
        try {
            clientRepository.add(mockClient);
            fail("Se esperaba un RuntimeException");
        } catch (RuntimeException e) {
            assertEquals("Error creating or executing the query: Error de conexión", e.getMessage());
        }
    }
    
    
    @Test
    public void removeTest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        clientRepository.remove(mockClient);
        
        verify(mockPreparedStatement).setString(1, "12345678A");
        verify(mockPreparedStatement).executeQuery();
    }
    
    @Test
    public void removeFailTest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Error de conexión"));
        
        try {
            clientRepository.remove(mockClient);
            fail("Se esperaba un RuntimeException");
        } catch (RuntimeException e) {
            assertEquals("Error creating or executing the query: Error de conexión", e.getMessage());
        }
    }
    
    
    @Test
    public void updateTest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        clientRepository.update(mockClient);
        
        verify(mockPreparedStatement).setString(1, "juanperez@example.com");
        verify(mockPreparedStatement).setString(2, "123456789");
        verify(mockPreparedStatement).setString(3, "Calle Falsa 123");
        verify(mockPreparedStatement).setString(4, "12345678A");
        verify(mockPreparedStatement).executeQuery();
    }
    
    @Test
    public void updateFailTest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Error de conexión"));
        
        try {
            clientRepository.update(mockClient);
            fail("Se esperaba un RuntimeException");
        } catch (RuntimeException e) {
            assertEquals("Error creating or executing the query: Error de conexión", e.getMessage());
        }
    }
    
    
    @Test
    public void searchClientExistTest() throws Exception {
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("Name")).thenReturn("Juan");
        when(mockResultSet.getString("Surname")).thenReturn("Pérez");
        when(mockResultSet.getString("NIF")).thenReturn("12345678A");
        when(mockResultSet.getString("Email")).thenReturn("juanperez@example.com");
        when(mockResultSet.getString("Phone")).thenReturn("123456789");
        when(mockResultSet.getString("Address")).thenReturn("Calle Falsa 123");
        when(mockResultSet.getString("BankName")).thenReturn("HappyBank");
        
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        
        ArrayList<Client> result = clientRepository.searchClient("12345678A", "Juan", "Pérez");
        
        assertNotNull(result);
        assertEquals("Juan", result.getFirst().getName());
        assertEquals("Pérez", result.getFirst().getSurname());
        assertEquals("12345678A", result.getFirst().getNIF());
        assertEquals("juanperez@example.com", result.getFirst().getEmail());
        assertEquals("123456789", result.getFirst().getPhone());
        assertEquals("Calle Falsa 123", result.getFirst().getAddress());
        assertEquals("HappyBank", result.getFirst().getBank());
    }
    
    @Test
    public void searchClientNotExistTest() throws Exception {
        when(mockResultSet.next()).thenReturn(false);
        
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        
        try {
            clientRepository.searchClient("12345678A", "Juan", "Pérez");
            fail("Se esperaba una RuntimeException porque el administrador no existe");
        } catch (RuntimeException e) {
            assertEquals("There are no clients.", e.getMessage());
        }
    }
    
    @Test
    public void searchClientFailTest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Error de conexión"));
        
        try {
            clientRepository.searchClient("12345678A", "Juan", "Pérez");
            fail("Se esperaba una SQLException");
        } catch (RuntimeException e) {
            assertEquals("Error creating or executing the query: Error de conexión", e.getMessage());
        }
    }
    
    
    @Test
    public void getExistsTest() throws Exception {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("Name")).thenReturn("Juan");
        when(mockResultSet.getString("Surname")).thenReturn("Pérez");
        when(mockResultSet.getString("NIF")).thenReturn("12345678A");
        when(mockResultSet.getString("Email")).thenReturn("juanperez@example.com");
        when(mockResultSet.getString("Phone")).thenReturn("123456789");
        when(mockResultSet.getString("Address")).thenReturn("Calle Falsa 123");
        when(mockResultSet.getString("BankName")).thenReturn("HappyBank");
        
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        
        Client result = clientRepository.get("12345678A");
        
        assertNotNull(result);
        assertEquals("Juan", result.getName());
        assertEquals("Pérez", result.getSurname());
        assertEquals("12345678A", result.getNIF());
        assertEquals("juanperez@example.com", result.getEmail());
        assertEquals("123456789", result.getPhone());
        assertEquals("Calle Falsa 123", result.getAddress());
        assertEquals("HappyBank", result.getBank());
    }
    
    @Test
    public void getDoesNotExistTest() throws Exception {
        when(mockResultSet.next()).thenReturn(false);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        
        try {
            clientRepository.get("12345678A");
            fail("Se esperaba una RuntimeException porque el administrador no existe");
        } catch (RuntimeException e) {
            assertEquals("The client does not exist.", e.getMessage());
        }
    }
    
    @Test
    public void getFailTest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Error de conexión"));
        
        try {
            clientRepository.get("12345678A");
            fail("Se esperaba una SQLException");
        } catch (RuntimeException e) {
            assertEquals("Error creating or executing the query: Error de conexión", e.getMessage());
        }
    }
    
    
    @Test
    public void getAllExistsTest() throws Exception {
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("Name")).thenReturn("Juan");
        when(mockResultSet.getString("Surname")).thenReturn("Pérez");
        when(mockResultSet.getString("NIF")).thenReturn("12345678A");
        when(mockResultSet.getString("Email")).thenReturn("juanperez@example.com");
        when(mockResultSet.getString("Phone")).thenReturn("123456789");
        when(mockResultSet.getString("Address")).thenReturn("Calle Falsa 123");
        when(mockResultSet.getString("BankName")).thenReturn("HappyBank");
        
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        
        ArrayList<Client> result = clientRepository.getAll();
        
        assertNotNull(result);
        assertEquals("Juan", result.getFirst().getName());
        assertEquals("Pérez", result.getFirst().getSurname());
        assertEquals("12345678A", result.getFirst().getNIF());
        assertEquals("juanperez@example.com", result.getFirst().getEmail());
        assertEquals("123456789", result.getFirst().getPhone());
        assertEquals("Calle Falsa 123", result.getFirst().getAddress());
        assertEquals("HappyBank", result.getFirst().getBank());
    }
    
    @Test
    public void testGetAllNotExists() throws Exception {
        when(mockResultSet.next()).thenReturn(false);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        
        try {
            clientRepository.getAll();
            fail("Se esperaba una RuntimeException porque el cliente no existe");
        } catch (RuntimeException e) {
            assertEquals("There are no clients.", e.getMessage());
        }
    }
    
    @Test
    public void getAllFailTest() throws Exception {
        when(mockConnection.createStatement()).thenThrow(new SQLException("Error de conexión"));
        
        try {
            clientRepository.getAll();
            fail("Se esperaba una SQLException");
        } catch (RuntimeException e) {
            assertEquals("Error creating or executing the query: Error de conexión", e.getMessage());
        }
    }
}
