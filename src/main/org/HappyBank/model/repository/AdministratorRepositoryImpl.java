package org.HappyBank.model.repository;

import org.HappyBank.model.Administrator;

import java.sql.*;
import java.util.ArrayList;

/**
 * Clase que representa un repositorio de administradores.
 * Implementa la interfaz IRepository.
 *
 * @see org.HappyBank.model.repository.IRepository
 */
public class AdministratorRepositoryImpl implements IRepository<Administrator> {
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
     * Autentifica a un administrador del banco
     *
     * @param NIF      NIF del administrador
     * @param password Contraseña del administrador
     * @return True si sus credenciales son correctas, false en caso contrario
     */
    public boolean validateAdministrator(String NIF, String password) {
        try (PreparedStatement stmt = getConnection().prepareStatement("SELECT * FROM Administrators WHERE NIF=? AND Password=?")) {
            stmt.setString(1, NIF);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating or executing the query: " + e.getMessage());
        }
    }
    
    /**
     * Cambia la contraseña a un administrador
     *
     * @param NIF      NIF del administrador
     * @param password Nueva contraseña del administrador
     */
    public void changePassword(String NIF, String password) {
        try (PreparedStatement stmt = getConnection().prepareStatement("UPDATE Administrators SET Password=? WHERE NIF=?")) {
            stmt.setString(1, password);
            stmt.setString(2, NIF);
            
            stmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating or executing the query: " + e.getMessage());
        }
    }
    
    /**
     * Añade un administrador al repositorio.
     *
     * @param admin Administrador a añadir.
     */
    @Override
    public void add(Administrator admin) {
        try (PreparedStatement stmt = getConnection().prepareStatement("INSERT INTO Administrators(NIF, Name, Surname, SSN, Salary, BankName, Password) VALUES(?, ?, ?, ?, ?, ?, ?)")) {
            stmt.setString(1, admin.getNIF());
            stmt.setString(2, admin.getName());
            stmt.setString(3, admin.getSurname());
            stmt.setString(4, admin.getSSN());
            stmt.setBigDecimal(5, admin.getSalary());
            stmt.setString(6, admin.getBank());
            stmt.setString(7, "Password");
            
            stmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating or executing the query: " + e.getMessage());
        }
    }
    
    /**
     * Elimina un administrador del repositorio.
     *
     * @param admin Administrador a eliminar.
     */
    @Override
    public void remove(Administrator admin) {
        try (PreparedStatement stmt = getConnection().prepareStatement("DELETE FROM Administrators WHERE NIF=?")) {
            stmt.setString(1, admin.getNIF());
            
            stmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating or executing the query: " + e.getMessage());
        }
    }
    
    /**
     * Actualiza un administrador del repositorio.
     *
     * @param admin Administrador a actualizar.
     */
    @Override
    public void update(Administrator admin) {
        try (PreparedStatement stmt = getConnection().prepareStatement("UPDATE Administrators SET Salary=? WHERE NIF=?")) {
            stmt.setBigDecimal(1, admin.getSalary());
            stmt.setString(2, admin.getNIF());
            
            stmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating or executing the query: " + e.getMessage());
        }
    }
    
    /**
     * Obtiene un administrador del repositorio.
     *
     * @param NIF NIF del administrador a obtener.
     * @return Administrador obtenido.
     */
    @Override
    public Administrator get(String NIF) {
        try (PreparedStatement stmt = getConnection().prepareStatement("SELECT * FROM Administrators WHERE NIF=?")) {
            stmt.setString(1, NIF);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return createAdmin(rs);
            } else {
                throw new RuntimeException("The administrator does not exist.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating or executing the query: " + e.getMessage());
        }
    }
    
    /**
     * Obtiene todos los administradores del repositorio.
     *
     * @return Lista con todos los administradores del repositorio.
     */
    @Override
    public ArrayList<Administrator> getAll() {
        ArrayList<Administrator> list = new ArrayList<>();
        
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Administrators")) {
            
            while (rs.next()) {
                list.add(createAdmin(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating or executing the query: " + e.getMessage());
        }
        if (list.isEmpty()) {
            throw new RuntimeException("There are no administrators.");
        }
        
        return list;
    }
    
    /**
     * Crea un administrador a partir del ResultSet
     *
     * @param rs ResultSet de la query con los datos del administrador
     * @return Administrador creado
     * @throws SQLException Si el ResultSet está vacío
     */
    private Administrator createAdmin(ResultSet rs) throws SQLException {
        return new Administrator(
                rs.getString("Name"),
                rs.getString("Surname"),
                rs.getString("NIF"),
                rs.getString("SSN"),
                rs.getBigDecimal("Salary"),
                rs.getString("BankName")
        );
    }
}
