package org.HappyBank.model.repository;

import java.sql.*;

/**
 * Clase que gestiona la base de datos.
 */
public class DatabaseManager {
    //Attributes
    /**
     * URL de la base de datos.
     */
    private static final String URL = "jdbc:mariadb://josedm64rpi.ddns.net:3306/HappyBank";
    /**
     * Nombre de usuario de la base de datos.
     */
    private static final String USERNAME = "INSO";
    /**
     * Contraseña de la base de datos.
     */
    private static final String PASSWORD = "INSO.";
    /**
     * Conexión con la base de datos.
     */
    private static Connection connection;
    
    
    //Singleton
    /**
     * Crea la instancia de la base de datos.
     *
     * @return Conexión con la base de datos.
     * @throws SQLException Si no es posible conectarse a la base de datos.
     */
    public static Connection getInstance() throws SQLException {
        if (connection == null) {
            new DatabaseManager();
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        }
        return connection;
    }
    
    private DatabaseManager() {}
}
