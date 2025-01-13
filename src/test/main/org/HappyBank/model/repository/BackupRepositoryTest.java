package org.HappyBank.model.repository;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.sql.*;

import static org.HappyBank.model.repository.BackupRepository.setBackupDirectory;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BackupRepositoryTest {
    BackupRepository backupRepository;
    ResultSetMetaData mockMetaData;
    Connection mockConnection;
    Statement mockStatement;
    ResultSet mockResultSet;
    
    @Before
    public void setUp() throws Exception {
        backupRepository = spy(BackupRepository.class);
        mockMetaData = mock(ResultSetMetaData.class);
        mockConnection = mock(Connection.class);
        mockStatement = mock(Statement.class);
        mockResultSet = mock(ResultSet.class);
        
        doReturn(mockConnection).when(backupRepository).getConnection();
    }
    
    @Test
    public void getConnectionTest() throws Exception {
        BackupRepository backupRepositoryTrue = new BackupRepository();
        Connection connection = backupRepositoryTrue.getConnection();
        assertNotNull(connection);
    }
    
    @Test
    public void backupTest() throws Exception {
        ensureBackupDirectoryExists();
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        
        when(mockStatement.executeQuery("SHOW TABLES")).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString(1)).thenReturn("Clients");
        
        ResultSet mockClientResultSet = mock(ResultSet.class);
        when(mockStatement.executeQuery("SELECT * FROM Clients")).thenReturn(mockClientResultSet);
        
        ResultSetMetaData mockMetaData = mock(ResultSetMetaData.class);
        when(mockClientResultSet.getMetaData()).thenReturn(mockMetaData);
        when(mockMetaData.getColumnCount()).thenReturn(2);
        when(mockMetaData.getColumnName(1)).thenReturn("NIF");
        when(mockMetaData.getColumnName(2)).thenReturn("Name");
        
        when(mockClientResultSet.next()).thenReturn(true, true, false);
        
        when(mockClientResultSet.getString(1))
                .thenReturn("12345678A")
                .thenReturn("87654321B");
        when(mockClientResultSet.getString(2))
                .thenReturn("Juan")
                .thenReturn("Maria");
        
        setBackupDirectory("target/etc/backup/");
        backupRepository.backupDatabase();
        
        verify(mockStatement).executeQuery("SHOW TABLES");
        verify(mockStatement).executeQuery("SELECT * FROM Clients");
        verify(mockClientResultSet).getMetaData();
        verify(mockClientResultSet, times(2)).getString(1); // Dos invocaciones para columna 1
        verify(mockClientResultSet, times(2)).getString(2); // Dos invocaciones para columna 2
    }
    
    @Test
    public void addFailTest() throws Exception {
        when(mockConnection.createStatement()).thenThrow(new SQLException("Error de conexión"));
        
        try {
            backupRepository.backupDatabase();
            fail("Se esperaba un RuntimeException");
        } catch (RuntimeException e) {
            assertEquals("Error creating or executing the query: Error de conexión", e.getMessage());
        }
    }
    
    private void ensureBackupDirectoryExists() {
        File directory = new File("target/etc/backup/");
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new RuntimeException("No se pudo crear la carpeta de respaldo en: " + "target/etc/backup/");
            }
        }
    }
}
