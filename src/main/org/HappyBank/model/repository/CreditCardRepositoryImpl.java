package org.HappyBank.model.repository;

import org.HappyBank.model.Account;
import org.HappyBank.model.CreditCard;

import java.sql.*;
import java.util.ArrayList;

public class CreditCardRepositoryImpl implements IRepository<CreditCard> {
    /**
     * Repositorio de clientes
     */
    private AccountRepositoryImpl accountRepository;
    
    /**
     * Constructor de la clase
     */
    public CreditCardRepositoryImpl() {
        this.accountRepository = new AccountRepositoryImpl();
    }
    
    /**
     * Establece el repositorio de clientes
     *
     * @param accountRepository Repositorio de clientes
     */
    public void setAccountRepository(AccountRepositoryImpl accountRepository) {
        this.accountRepository = accountRepository;
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
     * Añade una tarjeta al repositorio.
     *
     * @param card Tarjeta a añadir.
     */
    @Override
    public void add(CreditCard card) {
        try (PreparedStatement stmt = getConnection().prepareStatement("INSERT INTO CreditCards(Number, AccountIBAN, ExpirationDate, CVV) VALUES(?, ?, ?, ?) ")) {
            stmt.setString(1, card.getNumber());
            stmt.setString(2, card.getAccount().getIBAN());
            stmt.setDate(3, Date.valueOf(card.getExpirationDate()));
            stmt.setString(4, card.getCVV());
            
            stmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating or executing the query: " + e.getMessage());
        }
    }
    
    /**
     * Elimina una tarjeta del repositorio.
     *
     * @param card Tarjeta a eliminar.
     */
    @Override
    public void remove(CreditCard card) {
        try (PreparedStatement stmt = getConnection().prepareStatement("DELETE  FROM CreditCards WHERE Number=?")) {
            stmt.setString(1, card.getNumber());
            
            stmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating or executing the query: " + e.getMessage());
        }
    }
    
    /**
     * Actualiza una tarjeta del repositorio.
     *
     * @param card Tarjeta a actualizar.
     */
    @Override
    public void update(CreditCard card) {
        throw new UnsupportedOperationException("This method is unsupported.");
    }
    
    /**
     * Obtiene una tarjeta del repositorio.
     *
     * @param number Identificador de la tarjeta a obtener.
     * @return Tarjeta obtenida.
     */
    @Override
    public CreditCard get(String number) {
        try (PreparedStatement stmt = getConnection().prepareStatement("SELECT * FROM CreditCards WHERE Number=?")) {
            stmt.setString(1, number);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return createCard(rs);
            } else {
                throw new RuntimeException("The credit card does not exist.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating or executing the query: " + e.getMessage());
        }
    }
    
    /**
     * Obtiene una tarjeta del repositorio.
     *
     * @param account Cuenta asociada a la tarjeta.
     * @return Tarjeta obtenida.
     */
    public CreditCard get(Account account) {
        try (PreparedStatement stmt = getConnection().prepareStatement("SELECT * FROM CreditCards WHERE AccountIBAN=?")) {
            stmt.setString(1, account.getIBAN());
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return createCard(rs);
            } else {
                throw new RuntimeException("The credit card does not exist.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating or executing the query: " + e.getMessage());
        }
    }
    
    /**
     * Obtiene todos los objetos del repositorio.
     *
     * @return Lista con todos los objetos del repositorio.
     */
    @Override
    public ArrayList<CreditCard> getAll() {
        ArrayList<CreditCard> list = new ArrayList<>();
        
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM CreditCards")) {
            
            while (rs.next()) {
                list.add(createCard(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating or executing the query: " + e.getMessage());
        }
        if (list.isEmpty()) {
            throw new RuntimeException("There are no credit cards.");
        }
        
        return list;
    }
    
    /**
     * Comprueba si el número de tarjeta ya existe en el repositorio
     *
     * @param number Número de tarjeta a comprobar
     * @return True si existe y false en caso contrario
     */
    public boolean isNumber(String number) {
        try (PreparedStatement stmt = getConnection().prepareStatement("SELECT  * FROM CreditCards WHERE Number=?")) {
            stmt.setString(1, number);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating or executing the query: " + e.getMessage());
        }
    }
    
    /**
     * Crea una tarjeta de crédito a partir del ResultSet
     *
     * @param rs ResultSet de la query con los datos de la tarjeta de crédito
     * @return Tarjeta de crédito creado
     * @throws SQLException Si el ResultSet está vacío
     */
    private CreditCard createCard(ResultSet rs) throws SQLException {
        return new CreditCard(
                rs.getString("Number"),
                accountRepository.get(rs.getString("AccountIBAN")),
                rs.getDate("ExpirationDate").toLocalDate(),
                rs.getString("CVV")
        );
    }
}
