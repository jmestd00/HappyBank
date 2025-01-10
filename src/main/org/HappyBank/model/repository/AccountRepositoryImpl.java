package org.HappyBank.model.repository;

import org.HappyBank.model.Account;
import org.HappyBank.model.Client;

import java.sql.*;
import java.util.ArrayList;

public class AccountRepositoryImpl implements IRepository<Account> {
    /**
     * Repositorio de clientes
     */
    private ClientRepositoryImpl clientRepository;
    
    /**
     * Constructor de la clase
     */
    public AccountRepositoryImpl() {
        this.clientRepository = new ClientRepositoryImpl();
    }
    
    /**
     * Establece el repositorio de clientes
     *
     * @param clientRepository Repositorio de clientes
     */
    public void setClientRepository(ClientRepositoryImpl clientRepository) {
        this.clientRepository = clientRepository;
    }
    
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
     * Añade una cuenta al repositorio.
     *
     * @param account Cuenta a añadir.
     */
    @Override
    public void add(Account account) {
        try (PreparedStatement stmt = getConnection().prepareStatement("INSERT INTO Accounts(IBAN, OwnerNIF, Balance) VALUES (?, ?, ?)")) {
            stmt.setString(1, account.getIBAN());
            stmt.setString(2, account.getOwner().getNIF());
            stmt.setBigDecimal(3, account.getBalance());
            
            stmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating or executing the query: " + e.getMessage());
        }
    }
    
    /**
     * Elimina una cuenta del repositorio.
     *
     * @param account Cuenta a eliminar.
     */
    @Override
    public void remove(Account account) {
        try (PreparedStatement stmt = getConnection().prepareStatement("DELETE FROM Accounts WHERE IBAN=?")) {
            stmt.setString(1, account.getIBAN());
            
            stmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating or executing the query: " + e.getMessage());
        }
    }
    
    /**
     * Actualiza una cuenta del repositorio.
     *
     * @param account Cuenta a actualizar.
     */
    @Override
    public void update(Account account) {
        try (PreparedStatement stmt = getConnection().prepareStatement("UPDATE Accounts SET Balance=? WHERE IBAN=?")) {
            stmt.setBigDecimal(1, account.getBalance());
            stmt.setString(2, account.getIBAN());
            
            stmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating or executing the query: " + e.getMessage());
        }
    }
    
    /**
     * Obtiene una cuenta del repositorio.
     *
     * @param IBAN Identificador de la cuenta a obtener.
     * @return cuenta obtenido.
     */
    @Override
    public Account get(String IBAN) {
        try (PreparedStatement stmt = getConnection().prepareStatement("SELECT * FROM Accounts WHERE IBAN=?")) {
            stmt.setString(1, IBAN);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return createAccount(rs);
            } else {
                throw new RuntimeException("The account does not exist.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating or executing the query: " + e.getMessage());
        }
    }
    
    /**
     * Obtiene una cuenta del repositorio.
     *
     * @param client Cliente al que pertenece la cuenta
     * @return cuenta obtenido.
     */
    public Account get(Client client) {
        try (PreparedStatement stmt = getConnection().prepareStatement("SELECT * FROM Accounts WHERE OwnerNIF=?")) {
            stmt.setString(1, client.getNIF());
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return createAccount(rs);
            } else {
                throw new RuntimeException("The account does not exist.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating or executing the query: " + e.getMessage());
        }
    }
    
    /**
     * Obtiene todas las cuentas del repositorio.
     *
     * @return Lista con todas las cuentas del repositorio.
     */
    @Override
    public ArrayList<Account> getAll() {
        ArrayList<Account> list = new ArrayList<>();
        
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT  * FROM Accounts")) {
            
            while (rs.next()) {
                list.add(createAccount(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating or executing the query: " + e.getMessage());
        }
        if (list.isEmpty()) {
            throw new RuntimeException("There are no accounts.");
        }
        
        return list;
    }
    
    /**
     * Comprueba si el IBAN ya existe en el repositorio
     *
     * @param IBAN IBAN de la cuenta a comprobar
     * @return True si existe y false en caso contrario
     */
    public boolean isIBAN(String IBAN) {
        try (PreparedStatement stmt = getConnection().prepareStatement("SELECT * FROM Accounts WHERE IBAN=?")) {
            stmt.setString(1, IBAN);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating or executing the query: " + e.getMessage());
        }
    }
    
    /**
     * Crea una cuenta a partir del ResultSet
     *
     * @param rs ResultSet de la query con los datos de la cuenta
     * @return Cuenta creado
     * @throws SQLException Si el ResultSet está vacío
     */
    private Account createAccount(ResultSet rs) throws SQLException {
        return new Account(
                rs.getString("IBAN"),
                clientRepository.get(rs.getString("OwnerNIF")),
                rs.getBigDecimal("Balance")
        );
    }
}
