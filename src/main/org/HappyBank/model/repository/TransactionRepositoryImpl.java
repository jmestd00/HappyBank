package org.HappyBank.model.repository;

import org.HappyBank.model.Transaction;

import java.sql.*;
import java.util.ArrayList;

public class TransactionRepositoryImpl implements IRepository<Transaction> {
    /**
     * Devuelve una conexión a la base de datos
     *
     * @return Conexión a al base de datos
     * @throws SQLException Si no es posible conectarse
     */
    private Connection getConnection() throws SQLException {
        return DatabaseManager.getInstance();
    }
    
    /**
     * Añade una transacción al repositorio.
     *
     * @param transaction Transacción a añadir.
     */
    @Override
    public void add(Transaction transaction) {
        try (PreparedStatement stmt = getConnection().prepareStatement("INSERT INTO Transactions(Concept, Sender, Receiver, Amount, Date) VALUES(?, ?, ?, ?, ?)")) {
            stmt.setString(1, transaction.getConcept());
            stmt.setString(2, transaction.getSender().getIBAN());
            stmt.setString(3, transaction.getReceiver().getIBAN());
            stmt.setBigDecimal(4, transaction.getAmount());
            stmt.setTimestamp(5, Timestamp.valueOf(transaction.getDate()));
            
            stmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating or executing the query: " + e.getMessage());
        }
    }
    
    /**
     * Elimina una transacción del repositorio.
     *
     * @param transaction Transacción a eliminar.
     */
    @Override
    public void remove(Transaction transaction) {
        throw new UnsupportedOperationException("This method is unsupported.");
    }
    
    /**
     * Actualiza una transacción del repositorio.
     *
     * @param transaction Transacción a actualizar.
     */
    @Override
    public void update(Transaction transaction) {
        throw new UnsupportedOperationException("This method is unsupported.");
    }
    
    /**
     * Obtiene una transacción del repositorio.
     *
     * @param ID Identificador de la transacción a obtener.
     * @return Transacción obtenida.
     */
    @Override
    public Transaction get(String ID) {
        throw new UnsupportedOperationException("This method is unsupported.");
    }
    
    /**
     * Obtiene todas las transacciones del repositorio.
     *
     * @return Lista con todas las transacciones del repositorio.
     */
    @Override
    public ArrayList<Transaction> getAll() {
        ArrayList<Transaction> list = new ArrayList<>();
        
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT  * FROM Transactions")) {
            
            while (rs.next()) {
                list.add(createTransaction(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating or executing the query: " + e.getMessage());
        }
        
        return list;
    }
    
    /**
     * Obtiene las últimas 5 transacciones del repositorio.
     *
     * @return Lista con todas las transacciones del repositorio.
     */
    public ArrayList<Transaction> getLastTransactions(int quantity) {
        ArrayList<Transaction> list = new ArrayList<>();
        
        try (PreparedStatement stmt = getConnection().prepareStatement("SELECT * FROM Transactions ORDER BY Date DESC LIMIT ?")){
            stmt.setInt(1, quantity);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                list.add(createTransaction(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating or executing the query: " + e.getMessage());
        }
        
        return list;
    }
    
    /**
     * Crea una transacción a partir del ResultSet
     *
     * @param rs ResultSet de la query con los datos de la transacción
     * @return Transacción creada
     * @throws SQLException Si el ResultSet está vacío
     */
    private Transaction createTransaction(ResultSet rs) throws SQLException {
        AccountRepositoryImpl accountRepository = new AccountRepositoryImpl();
        
        return new Transaction(
                accountRepository.get(rs.getString("Sender")),
                accountRepository.get(rs.getString("Receiver")),
                rs.getString("Concept"),
                rs.getBigDecimal("Amount"),
                rs.getTimestamp("Date").toLocalDateTime()
        );
    }
}
