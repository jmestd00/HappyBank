package org.HappyBank.model;

import java.sql.*;
import java.util.ArrayList;

public class DataBase {
    //Datos
    private static final String URL = "jdbc:mariadb://josedm64rpi.ddns.net:3306/HappyBank";
    private static final String USERNAME = "INSO";
    private static final String PASSWORD = "INSO.";
    
    //Conexión
    private static Connection getConection() throws SQLException {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: no se pudo cargar el controlador de MariaDB.");
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
    
    //Métodos
    public static Client searchClient(String NIF) {
        Client client = null;
        String query = "SELECT * FROM Clients WHERE NIF = ?";
        
        try (Connection c = getConection(); PreparedStatement statement = c.prepareStatement(query)){
            statement.setString(1, NIF);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    client = new Client(resultSet.getString("NIF"), resultSet.getString("BankName"), resultSet.getString("FullName"), resultSet.getString("Mail"), resultSet.getString("PhoneNumber"), resultSet.getString("Address"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar el cliente: " + e.getMessage());
        }
        return client;
    }
    
    public static ArrayList<Client> getClients() {
        ArrayList<Client> list = new ArrayList<>();
        String query = "SELECT * FROM Clients";
        
        try (Connection c = getConection(); PreparedStatement statement = c.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                list.add(new Client(resultSet.getString("NIF"), resultSet.getString("BankName"), resultSet.getString("FullName"), resultSet.getString("Mail"), resultSet.getString("PhoneNumber"), resultSet.getString("Address")));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los clientes.");
        }
        return list;
    }
}
