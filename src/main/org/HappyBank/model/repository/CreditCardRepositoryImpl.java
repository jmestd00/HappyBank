package org.HappyBank.model.repository;

import org.HappyBank.model.Account;
import org.HappyBank.model.CreditCard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;

/**
 * Clase que representa un repositorio de tarjetas de crédito.
 * Implementa la interfaz IRepository.
 *
 * @see org.HappyBank.model.repository.IRepository
 */
public class CreditCardRepositoryImpl implements IRepository<CreditCard> {
    /**
     * Logger de la clase
     */
    private static final Logger logger = LogManager.getLogger(CreditCardRepositoryImpl.class);
    
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
            logger.info("Se ha creado una tarjeta de crédito con número {}.", card.getNumber());
        } catch (SQLException e) {
            logger.error("Error con la consulta a la base de datos: {}", e.getMessage());
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
            logger.info("Se ha eliminado la tarjeta de crédito con número {}.", card.getNumber());
        } catch (SQLException e) {
            logger.error("Error con la consulta a la base de datos: {}", e.getMessage());
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
        logger.error("Se ha accedido a una operación no permitida: update.");
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
                logger.info("Obtenida la tarjeta de crédito con número {}.", number);
                return createCard(rs);
            } else {
                logger.error("No se ha encontrado la tarjeta de crédito con número {}.", number);
                throw new RuntimeException("The credit card does not exist.");
            }
        } catch (SQLException e) {
            logger.error("Error con la consulta a la base de datos: {}", e.getMessage());
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
                logger.info("Obtenida la tarjeta de crédito asociada a la cuenta con IBAN: {}.", account.getIBAN());
                return createCard(rs);
            } else {
                logger.error("No se ha encontrado la tarjeta de crédito asociada a la cuenta con IBAN: {}.", account.getIBAN());
                throw new RuntimeException("The credit card does not exist.");
            }
        } catch (SQLException e) {
            logger.error("Error con la consulta a la base de datos: {}", e.getMessage());
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
            logger.error("Error con la consulta a la base de datos: {}", e.getMessage());
            throw new RuntimeException("Error creating or executing the query: " + e.getMessage());
        }
        if (list.isEmpty()) {
            logger.error("No hay tarjetas de crédito.");
            throw new RuntimeException("There are no credit cards.");
        }
        
        logger.info("Obtenidas todas las tarjetas de crédito.");
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
