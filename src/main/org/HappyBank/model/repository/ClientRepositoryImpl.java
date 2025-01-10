package org.HappyBank.model.repository;

import org.HappyBank.model.Client;

import java.sql.*;
import java.util.ArrayList;

public class ClientRepositoryImpl implements IRepository<Client> {
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
     * Autentifica a un cliente del banco
     *
     * @param NIF      NIF del cliente
     * @param password Contraseña del cliente
     * @return True si sus credenciales son correctas, false en caso contrario
     */
    public Boolean validateClient(String NIF, String password) {
        try (PreparedStatement stmt = getConnection().prepareStatement("SELECT * FROM Clients WHERE NIF=? AND Password=?")) {
            stmt.setString(1, NIF);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating or executing the query: " + e.getMessage());
        }
    }
    
    /**
     * Cambia la contraseña a un cliente
     *
     * @param NIF      NIF del cliente
     * @param password Nueva contraseña del cliente
     */
    public void changePassword(String NIF, String password) {
        try (PreparedStatement stmt = getConnection().prepareStatement("UPDATE Clients SET Password=? WHERE NIF=?")) {
            stmt.setString(1, password);
            stmt.setString(2, NIF);
            
            stmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating or executing the query: " + e.getMessage());
        }
    }
    
    /**
     * Añade un cliente al repositorio.
     *
     * @param client Cliente a añadir.
     */
    @Override
    public void add(Client client) {
        try (PreparedStatement stmt = getConnection().prepareStatement("INSERT INTO Clients (NIF, Name, Surname, Email, Phone, Address, BankName, Password) VALUES(?, ?, ?, ?, ?, ?, ?, ?)")) {
            stmt.setString(1, client.getNIF());
            stmt.setString(2, client.getName());
            stmt.setString(3, client.getSurname());
            stmt.setString(4, client.getEmail());
            stmt.setString(5, client.getPhone());
            stmt.setString(6, client.getAddress());
            stmt.setString(7, client.getBank());
            stmt.setString(8, "Password");
            
            stmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating or executing the query: " + e.getMessage());
        }
    }
    
    /**
     * Elimina un cliente del repositorio.
     *
     * @param client Cliente a eliminar.
     */
    @Override
    public void remove(Client client) {
        try (PreparedStatement stmt = getConnection().prepareStatement("DELETE FROM Clients WHERE NIF=?")) {
            stmt.setString(1, client.getNIF());
            
            stmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating or executing the query: " + e.getMessage());
        }
    }
    
    /**
     * Actualiza un cliente del repositorio.
     *
     * @param client Cliente a actualizar.
     */
    @Override
    public void update(Client client) {
        try (PreparedStatement stmt = getConnection().prepareStatement("UPDATE Clients SET Email=?, Phone=?, Address=? WHERE NIF = ?")) {
            stmt.setString(1, client.getEmail());
            stmt.setString(2, client.getPhone());
            stmt.setString(3, client.getAddress());
            stmt.setString(4, client.getNIF());
            
            stmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating or executing the query: " + e.getMessage());
        }
    }
    
    /**
     * Obtiene todos los clientes según datos incompletos del cliente
     *
     * @param NIF     NIF del cliente
     * @param name    Nombre del Cliente
     * @param surname Apellido del cliente
     * @return Lista de clientes que encajan en la descripción
     */
    public ArrayList<Client> searchClient(String NIF, String name, String surname) {
        ArrayList<Client> list = new ArrayList<>();
        
        try (PreparedStatement stmt = getConnection().prepareStatement("SELECT * FROM Clients WHERE NIF LIKE ? OR Name LIKE ? OR Surname LIKE ?")) {
            stmt.setString(1, NIF);
            stmt.setString(2, name + "%");
            stmt.setString(3, surname + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                list.add(createClient(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error creating or executing the query: " + e.getMessage());
        }
        if (list.isEmpty()) {
            throw new RuntimeException("There are no clients.");
        }
        
        return list;
    }
    
    /**
     * Obtiene un cliente del repositorio.
     *
     * @param NIF Identificador del Cliente a obtener.
     * @return Cliente.
     */
    @Override
    public Client get(String NIF) {
        try (PreparedStatement stmt = getConnection().prepareStatement("SELECT * FROM Clients WHERE NIF=?")) {
            stmt.setString(1, NIF);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return createClient(rs);
            } else {
                throw new RuntimeException("The client does not exist.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating or executing the query: " + e.getMessage());
        }
    }
    
    /**
     * Obtiene todos los clientes del repositorio.
     *
     * @return Lista con todos los clientes del repositorio.
     */
    @Override
    public ArrayList<Client> getAll() {
        ArrayList<Client> list = new ArrayList<>();
        
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Clients")) {
            
            while (rs.next()) {
                list.add(createClient(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating or executing the query: " + e.getMessage());
        }
        if (list.isEmpty()) {
            throw new RuntimeException("There are no clients.");
        }
        
        return list;
    }
    
    /**
     * Crea un cliente a partir del ResultSet
     *
     * @param rs ResultSet de la query con los datos del cliente
     * @return Cliente creado
     * @throws SQLException Si el ResultSet está vacío
     */
    private Client createClient(ResultSet rs) throws SQLException {
        return new Client(
                rs.getString("Name"),
                rs.getString("Surname"),
                rs.getString("NIF"),
                rs.getString("Email"),
                rs.getString("Phone"),
                rs.getString("Address"),
                rs.getString("BankName")
        );
    }
}
