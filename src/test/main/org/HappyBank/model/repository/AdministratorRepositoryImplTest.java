package org.HappyBank.model.repository;

import org.HappyBank.model.Administrator;
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

public class AdministratorRepositoryImplTest {
    private AdministratorRepositoryImpl administratorRepository;
    private PreparedStatement mockPreparedStatement;
    private Statement mockStatement;
    private Connection mockConnection;
    private ResultSet mockResultSet;
    private Administrator mockAdmin;
    
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
        
        administratorRepository = spy(AdministratorRepositoryImpl.class);
        doReturn(mockConnection).when(administratorRepository).getConnection();
        
        mockAdmin = new Administrator("Juan", "Pérez", "12345678A", "1234567890", new BigDecimal(1200), "HappyBank");
    }
    
    
    @Test
    public void getConnectionTest() throws Exception {
        AdministratorRepositoryImpl administratorRepositoryTrue = new AdministratorRepositoryImpl();
        Connection connection = administratorRepositoryTrue.getConnection();
        assertNotNull(connection);
    }
    
    
    @Test
    public void validateAdministratorTest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        
        boolean exists = administratorRepository.validateAdministrator("12345678A", "Password");
        
        assertTrue(exists);
        verify(mockPreparedStatement).setString(1, "12345678A");
        verify(mockPreparedStatement).setString(2, "Password");
        verify(mockPreparedStatement).executeQuery();
    }
    
    @Test
    public void validateAdministratorFailTest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Error de conexión"));
        
        try {
            administratorRepository.validateAdministrator("12345678A", "Password");
            fail("Se esperaba un RuntimeException");
        } catch (RuntimeException e) {
            assertEquals("Error creating or executing the query: Error de conexión", e.getMessage());
        }
    }
    
    
    @Test
    public void changePasswordTest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        administratorRepository.changePassword("12345678A", "Password2");
        
        verify(mockPreparedStatement).setString(1, "Password2");
        verify(mockPreparedStatement).setString(2, "12345678A");
        verify(mockPreparedStatement).executeQuery();
    }
    
    @Test
    public void changePasswordFailTest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Error de conexión"));
        
        try {
            administratorRepository.changePassword("12345678A", "Password2");
            fail("Se esperaba un RuntimeException");
        } catch (RuntimeException e) {
            assertEquals("Error creating or executing the query: Error de conexión", e.getMessage());
        }
    }
    
    
    @Test
    public void addTest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        administratorRepository.add(mockAdmin);
        
        verify(mockPreparedStatement).setString(1, "12345678A");
        verify(mockPreparedStatement).setString(2, "Juan");
        verify(mockPreparedStatement).setString(3, "Pérez");
        verify(mockPreparedStatement).setString(4, "1234567890");
        verify(mockPreparedStatement).setBigDecimal(5, new BigDecimal(1200));
        verify(mockPreparedStatement).setString(6, "HappyBank");
        verify(mockPreparedStatement).setString(7, "Password");
        verify(mockPreparedStatement).executeQuery();
    }
    
    @Test
    public void addFailTest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Error de conexión"));
        
        try {
            administratorRepository.add(mockAdmin);
            fail("Se esperaba un RuntimeException");
        } catch (RuntimeException e) {
            assertEquals("Error creating or executing the query: Error de conexión", e.getMessage());
        }
    }
    
    
    @Test
    public void removeTest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        administratorRepository.remove(mockAdmin);
        
        verify(mockPreparedStatement).setString(1, "12345678A");
        verify(mockPreparedStatement).executeQuery();
    }
    
    @Test
    public void removeFailTest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Error de conexión"));
        
        try {
            administratorRepository.remove(mockAdmin);
            fail("Se esperaba un RuntimeException");
        } catch (RuntimeException e) {
            assertEquals("Error creating or executing the query: Error de conexión", e.getMessage());
        }
    }
    
    
    @Test
    public void updateTest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        administratorRepository.update(mockAdmin);
        
        verify(mockPreparedStatement).setBigDecimal(1, new BigDecimal(1200));
        verify(mockPreparedStatement).setString(2, "12345678A");
        verify(mockPreparedStatement).executeQuery();
    }
    
    @Test
    public void updateFailTest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Error de conexión"));
        
        try {
            administratorRepository.update(mockAdmin);
            fail("Se esperaba un RuntimeException");
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
        when(mockResultSet.getString("SSN")).thenReturn("1234567890");
        when(mockResultSet.getBigDecimal("Salary")).thenReturn(new BigDecimal(1200));
        when(mockResultSet.getString("BankName")).thenReturn("HappyBank");
        
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        
        Administrator result = administratorRepository.get("12345678A");
        
        assertNotNull(result);
        assertEquals("Juan", result.getName());
        assertEquals("Pérez", result.getSurname());
        assertEquals("12345678A", result.getNIF());
        assertEquals("1234567890", result.getSSN());
        assertEquals(new BigDecimal(1200), result.getSalary());
        assertEquals("HappyBank", result.getBank());
    }
    
    @Test
    public void getDoesNotExistTest() throws Exception {
        when(mockResultSet.next()).thenReturn(false);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        
        try {
            administratorRepository.get("12345678A");
            fail("Se esperaba una RuntimeException porque el administrador no existe");
        } catch (RuntimeException e) {
            assertEquals("The administrator does not exist.", e.getMessage());
        }
    }
    
    @Test
    public void getFailTest() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Error de conexión"));
        
        try {
            administratorRepository.get("12345678A");
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
        when(mockResultSet.getString("SSN")).thenReturn("1234567890");
        when(mockResultSet.getBigDecimal("Salary")).thenReturn(new BigDecimal(1200));
        when(mockResultSet.getString("BankName")).thenReturn("HappyBank");
        
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        
        ArrayList<Administrator> result = administratorRepository.getAll();
        
        assertNotNull(result);
        assertEquals("Juan", result.getFirst().getName());
        assertEquals("Pérez", result.getFirst().getSurname());
        assertEquals("12345678A", result.getFirst().getNIF());
        assertEquals("1234567890", result.getFirst().getSSN());
        assertEquals(new BigDecimal(1200), result.getFirst().getSalary());
        assertEquals("HappyBank", result.getFirst().getBank());
    }
    
    @Test
    public void testGetAllNotExists() throws Exception {
        when(mockResultSet.next()).thenReturn(false);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        
        try {
            administratorRepository.getAll();
            fail("Se esperaba una RuntimeException porque el administrador no existe");
        } catch (RuntimeException e) {
            assertEquals("There are no administrators.", e.getMessage());
        }
    }
    
    @Test
    public void getAllFailTest() throws Exception {
        when(mockConnection.createStatement()).thenThrow(new SQLException("Error de conexión"));
        
        try {
            administratorRepository.getAll();
            fail("Se esperaba una SQLException");
        } catch (RuntimeException e) {
            assertEquals("Error creating or executing the query: Error de conexión", e.getMessage());
        }
    }
}
