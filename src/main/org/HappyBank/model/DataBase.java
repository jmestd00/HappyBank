package org.HappyBank.model;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static java.sql.DriverManager.getConnection;

/**
 * Clase que gestiona la base de datos.
 */
public class DataBase {
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
     * Instancia de la base de datos.
     */
    private static DataBase database;
    /**
     * Conexión con la base de datos.
     */
    private static Connection connection;
    
    
    //Singleton
    /**
     * Constructor por defecto.
     * @throws HappyBankException Si ocurre un error al conectar con la base de datos.
     */
    private DataBase() throws HappyBankException {
        try {
            connection = getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new HappyBankException("Error connecting to the database: " + e.getMessage());
        }
    }
    
    /**
     * Crea la instancia de la base de datos.
     * @throws HappyBankException Si ocurre un error al conectar con la base de datos.
     */
    public static void getInstance() throws HappyBankException {
        if (database == null) {
            database = new DataBase();
        }
    }
    
    
    //Login
    /**
     * Comprueba si un cliente puede iniciar sesión.
     * @param NIF NIF del cliente.
     * @param password Contraseña del cliente.
     * @return True si el cliente puede iniciar sesión, false en caso contrario.
     * @throws HappyBankException Si ocurre un error al preparar o ejecutar la consulta.
     */
    public static boolean loginClient(String NIF, String password) throws HappyBankException {
        String query = "SELECT * FROM Clients WHERE NIF = ? AND Password = ?";
        PreparedStatement statement;
        
        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, NIF);
            statement.setString(2, password);
        } catch (SQLException e) {
            throw new HappyBankException("Error preparing the statement: " + e.getMessage());
        }
        
        try {
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new HappyBankException("Error executing the query: " + e.getMessage());
        }
    }
    
    /**
     * Comprueba si un administrador puede iniciar sesión.
     * @param NIF NIF del administrador.
     * @param password Contraseña del administrador.
     * @return True si el administrador puede iniciar sesión, false en caso contrario.
     * @throws HappyBankException Si ocurre un error al preparar o ejecutar la consulta.
     */
    public static boolean loginAdministrator(String NIF, String password) throws HappyBankException {
        String query = "SELECT * FROM Administrators WHERE NIF = ? AND Password = ?";
        PreparedStatement statement;
        
        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, NIF);
            statement.setString(2, password);
        } catch (SQLException e) {
            throw new HappyBankException("Error preparing the statement: " + e.getMessage());
        }
        
        try {
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new HappyBankException("Error executing the query: " + e.getMessage());
        }
    }
    
    
    //Clients
    /**
     * Inserta un cliente en la base de datos.
     * @param c Cliente a insertar.
     * @param password Contraseña del cliente.
     * @throws HappyBankException Si ocurre un error al preparar o ejecutar la consulta.
     */
    public static void insertClient(Client c, String password) throws HappyBankException {
        String query = "INSERT INTO Clients (NIF, Name, Surname, Email, Phone, Address, Password, BankName) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement;
        
        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, c.getNIF());
            statement.setString(2, c.getName());
            statement.setString(3, c.getSurname());
            statement.setString(4, c.getEmail());
            statement.setString(5, c.getPhone());
            statement.setString(6, c.getAddress());
            statement.setString(7, password);
            statement.setString(8, c.getBank());
        } catch (SQLException e) {
            throw new HappyBankException("Error preparing the statement: " + e.getMessage());
        }
        
        try {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new HappyBankException("Error executing the query: " + e.getMessage());
        }
    }
    
    /**
     * Elimina un cliente de la base de datos.
     * @param NIF NIF del cliente a eliminar.
     * @throws HappyBankException Si ocurre un error al preparar o ejecutar la consulta.
     */
    public static void deleteClient(String NIF) throws HappyBankException {
        String query = "DELETE FROM Clients WHERE NIF = ?";
        PreparedStatement statement;
        
        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, NIF);
        } catch (SQLException e) {
            throw new HappyBankException("Error preparing the statement: " + e.getMessage());
        }
        
        try {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new HappyBankException("Error executing the query: " + e.getMessage());
        }
    }
    
    /**
     * Devuelve un cliente concreto de la base de datos.
     * @param NIF NIF del cliente
     * @return Cliente con el NIF especificado.
     * @throws HappyBankException Si ocurre un error al preparar o ejecutar la consulta.
     */
    public static Client getClient(String NIF) throws HappyBankException {
        Client client;
        String query = "SELECT * FROM Clients WHERE NIF = ?";
        PreparedStatement statement;
        
        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, NIF);
        } catch (SQLException e) {
            throw new HappyBankException("Error preparing the statement: " + e.getMessage());
        }
        
        try {
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                String name = resultSet.getString("Name");
                String surname = resultSet.getString("Surname");
                String email = resultSet.getString("Email");
                String phone = resultSet.getString("Phone");
                String address = resultSet.getString("Address");
                String bank = resultSet.getString("BankName");
                
                client = new Client(name, surname, NIF, email, phone, address, bank);
            } else {
                throw new HappyBankException("Client not found");
            }
        } catch (SQLException e) {
            throw new HappyBankException("Error executing the query: " + e.getMessage());
        }
        
        return client;
    }
    
    
    //Administrators
    /**
     * Inserta un administrador en la base de datos.
     * @param a Administrador a insertar.
     * @param password Contraseña del administrador.
     * @throws HappyBankException Si ocurre un error al preparar o ejecutar la consulta.
     */
    public static void insertAdministrator(Administrator a, String password) throws HappyBankException {
        String query = "INSERT INTO Administrators (NIF, Name, Surname, SSN, Salary, BankName, Password) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement;
        
        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, a.getNIF());
            statement.setString(2, a.getName());
            statement.setString(3, a.getSurname());
            statement.setString(4, a.getSSN());
            statement.setBigDecimal(5, a.getSalary());
            statement.setString(6, a.getBank());
            statement.setString(7, password);
        } catch (SQLException e) {
            throw new HappyBankException("Error preparing the statement: " + e.getMessage());
        }
        
        try {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new HappyBankException("Error executing the query: " + e.getMessage());
        }
    }
    
    /**
     * Elimina un administrador de la base de datos.
     * @param NIF NIF del administrador a eliminar.
     * @throws HappyBankException Si ocurre un error al preparar o ejecutar la consulta.
     */
    public static void deleteAdministrator(String NIF) throws HappyBankException {
        String query = "DELETE FROM Administrators WHERE NIF = ?";
        PreparedStatement statement;
        
        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, NIF);
        } catch (SQLException e) {
            throw new HappyBankException("Error preparing the statement: " + e.getMessage());
        }
        
        try {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new HappyBankException("Error executing the query: " + e.getMessage());
        }
    }
    
    /**
     * Devuelve un administrador concreto de la base de datos.
     * @param NIF NIF del administrador
     * @return Administrador con el NIF especificado.
     * @throws HappyBankException Si ocurre un error al preparar o ejecutar la consulta.
     */
    public static Administrator getAdministrator(String NIF) throws HappyBankException {
        Administrator administrator;
        String query = "SELECT * FROM Administrators WHERE NIF = ?";
        PreparedStatement statement;
        
        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, NIF);
        } catch (SQLException e) {
            throw new HappyBankException("Error preparing the statement: " + e.getMessage());
        }
        
        try {
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                String name = resultSet.getString("Name");
                String surname = resultSet.getString("Surname");
                String SSN = resultSet.getString("SSN");
                BigDecimal salary = resultSet.getBigDecimal("Salary");
                String bank = resultSet.getString("BankName");
                
                administrator = new Administrator(name, surname, NIF, SSN, salary, bank);
            } else {
                throw new HappyBankException("Administrator not found");
            }
        } catch (SQLException e) {
            throw new HappyBankException("Error executing the query: " + e.getMessage());
        }
        
        return administrator;
    }
    
    
    //Accounts
    /**
     * Inserta una cuenta bancaria en la base de datos.
     * @param a Cuenta a insertar.
     * @throws HappyBankException Si ocurre un error al preparar o ejecutar la consulta.
     */
    public static void insertAccount(Account a) throws HappyBankException {
        String query = "INSERT INTO Accounts (IBAN, OwnerNIF, Balance) VALUES (?, ?, ?)";
        PreparedStatement statement;
        
        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, a.getIBAN());
            statement.setString(2, a.getOwnerNIF());
            statement.setBigDecimal(3, a.getBalance());
        } catch (SQLException e) {
            throw new HappyBankException("Error preparing the statement: " + e.getMessage());
        }
        
        try {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new HappyBankException("Error executing the query: " + e.getMessage());
        }
    }
    
    /**
     * Devuelve una cuenta bancaria
     * @param IBAN Número de la cuenta
     * @return Cuenta con el IBAN especificado.
     * @throws HappyBankException Si ocurre un error al preparar o ejecutar la consulta.
     */
    public static Account getAccount(String IBAN) throws HappyBankException {
        Account account;
        String query = "SELECT * FROM Accounts WHERE IBAN = ?";
        PreparedStatement statement;
        
        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, IBAN);
        } catch (SQLException e) {
            throw new HappyBankException("Error preparing the statement: " + e.getMessage());
        }
        
        try {
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                String ownerNIF = resultSet.getString("OwnerNIF");
                BigDecimal balance = resultSet.getBigDecimal("Balance");
                
                account = new Account(IBAN, ownerNIF, balance);
            } else {
                throw new HappyBankException("Account not found");
            }
        } catch (SQLException e) {
            throw new HappyBankException("Error executing the query: " + e.getMessage());
        }
        
        return account;
    }
    
    /**
     * Elimina una cuenta bancaria de la base de datos.
     * @param IBAN Número de la cuenta a eliminar.
     * @throws HappyBankException Si ocurre un error al preparar o ejecutar la consulta.
     */
    public static void deleteAccount(String IBAN) throws HappyBankException {
        String query = "DELETE FROM Accounts WHERE IBAN = ?";
        PreparedStatement statement;
        
        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, IBAN);
        } catch (SQLException e) {
            throw new HappyBankException("Error preparing the statement: " + e.getMessage());
        }
        
        try {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new HappyBankException("Error executing the query: " + e.getMessage());
        }
    }
    
    
    //Transactions
    /**
     * Inserta una transacción en la base de datos
     * @param t Transacción a insertar.
     * @throws HappyBankException Si ocurre un error al preparar o ejecutar la consulta.
     */
    public static void insertTransaction(Transaction t) throws HappyBankException {
        String query = "INSERT INTO Transactions (ID, Sender, Receiver, Concept, Amount, Date) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement statement;
        
        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, String.valueOf(t.getID()));
            statement.setString(2, t.getSender().getIBAN());
            statement.setString(3, t.getReceiver().getIBAN());
            statement.setString(4, t.getConcept());
            statement.setBigDecimal(5, t.getAmount());
            statement.setTimestamp(6, Timestamp.valueOf(t.getDate()));
        } catch (SQLException e) {
            throw new HappyBankException("Error preparing the statement: " + e.getMessage());
        }
        
        try {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new HappyBankException("Error executing the query: " + e.getMessage());
        }
    }
    
    /**
     * Elimina una transacción de la base de datos.
     * @param ID Ident
     * @throws HappyBankException Si ocurre un error al preparar o ejecutar la consulta.
     */
    public static void deleteTransaction(int ID) throws HappyBankException {
        String query = "DELETE FROM Transactions WHERE ID = ?";
        PreparedStatement statement;
        
        try {
            statement = connection.prepareStatement(query);
            statement.setInt(1, ID);
        } catch (SQLException e) {
            throw new HappyBankException("Error preparing the statement: " + e.getMessage());
        }
        
        try {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new HappyBankException("Error executing the query: " + e.getMessage());
        }
    }
    
    /**
     * Devuelve una transacción concreta de la base de datos.
     * @param ID Identificador de la transacción.
     * @return Transacción con el ID especificado.
     * @throws HappyBankException Si ocurre un error al preparar o ejecutar la consulta.
     */
    public static Transaction getTransaction(int ID) throws HappyBankException {
        Transaction transaction;
        String query = "SELECT * FROM Transactions WHERE ID = ?";
        PreparedStatement statement;
        
        try {
            statement = connection.prepareStatement(query);
            statement.setInt(1, ID);
        } catch (SQLException e) {
            throw new HappyBankException("Error preparing the statement: " + e.getMessage());
        }
        
        try {
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                Account sender = getAccount(resultSet.getString("Sender"));
                Account receiver = getAccount(resultSet.getString("Receiver"));
                String concept = resultSet.getString("Concept");
                BigDecimal amount = resultSet.getBigDecimal("Amount");
                LocalDateTime date = resultSet.getTimestamp("Date").toLocalDateTime();
                
                transaction = new Transaction(ID, sender, receiver, concept, amount, date);
            } else {
                throw new HappyBankException("Transaction not found");
            }
        } catch (SQLException e) {
            throw new HappyBankException("Error executing the query: " + e.getMessage());
        }
        
        return transaction;
    }
    
    /**
     * Obtiene las últimas 5  transacciones realizadas
     * @return Lista con las últimas 5 transacciones realizadas.
     * @throws HappyBankException Si ocurre un error al preparar o ejecutar la consulta.
     */
    public static ArrayList<Transaction> getLastTransactions() throws HappyBankException {
        ArrayList<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM Transactions ORDER BY Date DESC LIMIT 5";
        PreparedStatement statement;
        
        try {
            statement = connection.prepareStatement(query);
        } catch (SQLException e) {
            throw new HappyBankException("Error preparing the statement: " + e.getMessage());
        }
        
        try {
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                Account sender = getAccount(resultSet.getString("Sender"));
                Account receiver = getAccount(resultSet.getString("Receiver"));
                String concept = resultSet.getString("Concept");
                BigDecimal amount = resultSet.getBigDecimal("Amount");
                LocalDateTime date = resultSet.getTimestamp("Date").toLocalDateTime();
                
                transactions.add(new Transaction(id, sender, receiver, concept, amount, date));
            }
        } catch (SQLException e) {
            throw new HappyBankException("Error executing the query: " + e.getMessage());
        }
        
        return transactions;
    }
    
    /**
     * Obtiene todas las transacciones realizadas
     * @return Lista con todas las transacciones realizadas.
     * @throws HappyBankException Si ocurre un error al preparar o ejecutar la consulta.
     */
    public static ArrayList<Transaction> getAllTransactions() throws HappyBankException {
        ArrayList<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM Transactions";
        PreparedStatement statement;
        
        try {
            statement = connection.prepareStatement(query);
        } catch (SQLException e) {
            throw new HappyBankException("Error preparing the statement: " + e.getMessage());
        }
        
        try {
            ResultSet resultSet = statement.executeQuery();
            
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                Account sender = getAccount(resultSet.getString("Sender"));
                Account receiver = getAccount(resultSet.getString("Receiver"));
                String concept = resultSet.getString("Concept");
                BigDecimal amount = resultSet.getBigDecimal("Amount");
                LocalDateTime date = resultSet.getTimestamp("Date").toLocalDateTime();
                
                transactions.add(new Transaction(id, sender, receiver, concept, amount, date));
            }
        } catch (SQLException e) {
            throw new HappyBankException("Error executing the query: " + e.getMessage());
        }
        
        return transactions;
    }
}
