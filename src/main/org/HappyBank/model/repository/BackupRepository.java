package org.HappyBank.model.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatterBuilder;

public class BackupRepository {
    /**
     * Logger para la clase.
     */
    private static final Logger logger = LogManager.getLogger(BackupRepository.class);
    /**
     * Directorio de la copia de seguridad.
     */
    private static String BACKUP_DIR = "etc/backup/";
    /**
     * Fichero de la copia de seguridad.
     */
    private static final String BACKUP_FILE = "HappyBank_" + LocalDateTime.now().format(new DateTimeFormatterBuilder().appendPattern("dd-MM-yyy HH:mm:ss").toFormatter()) + ".csv";
    
    /**
     * Devuelve una conexión a la base de datos
     *
     * @return Conexión a al base de datos
     * @throws SQLException Si no es posible conectarse
     */
    protected Connection getConnection() throws SQLException {
        return DatabaseManager.getInstance();
    }
    
    /**
     * Establece el directorio de la copia de seguridad.
     *
     * @param backupDir Directorio de la copia de seguridad
     */
    public static void setBackupDirectory(String backupDir) {
        BACKUP_DIR = backupDir;
    }
    
    /**
     * Realiza una copia de seguridad de la base de datos.
     */
    public void backupDatabase() {
        try (Statement stmt = getConnection().createStatement()) {
            ResultSet tables = stmt.executeQuery("SHOW TABLES");
            
            while (tables.next()) {
                String tableName = tables.getString(1);
                String query = "SELECT * FROM " + tableName;
                
                ResultSet resultSet = stmt.executeQuery(query);
                
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(BACKUP_DIR + BACKUP_FILE, true))) {
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    
                    for (int i = 1; i <= columnCount; i++) {
                        writer.write(metaData.getColumnName(i));
                        if (i < columnCount) writer.write(",");
                    }
                    writer.newLine();
                    
                    while (resultSet.next()) {
                        for (int i = 1; i <= columnCount; i++) {
                            writer.write(resultSet.getString(i));
                            if (i < columnCount) writer.write(",");
                        }
                        writer.newLine();
                    }
                    writer.flush();
                }
            }
            logger.info("Copia de seguridad completada: {}", BACKUP_DIR + BACKUP_FILE);
        } catch (SQLException | IOException e) {
            logger.error("Error al generar la copia de seguridad.");
            throw new RuntimeException("Error creating or executing the query: " + e.getMessage());
        }
    }
}
