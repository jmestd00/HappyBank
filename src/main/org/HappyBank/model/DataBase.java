package org.HappyBank.model;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static java.sql.DriverManager.getConnection;

public class DataBase {
    //Attributes
    private static final String URL = "jdbc:mariadb://josedm64rpi.ddns.net:3306/HappyBank";
    private static final String USERNAME = "INSO";
    private static final String PASSWORD = "INSO.";
    
    private static DataBase database;
    private static Connection connection;
    
    
    //Singleton
    private DataBase() throws HappyBankException {
        try {
            connection = getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new HappyBankException("Error connecting to the database: " + e.getMessage());
        }
    }
    
    public static DataBase getInstance() throws HappyBankException {
        if (database == null) {
            database = new DataBase();
        }
        return database;
    }
    
    
    //Login
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
    
    
    //Queries
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
                Account sender = getAccount(resultSet.getString("SenderIBAN"));
                Account receiver = getAccount(resultSet.getString("ReceiverIBAN"));
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
                Account sender = getAccount(resultSet.getString("SenderIBAN"));
                Account receiver = getAccount(resultSet.getString("ReceiverIBAN"));
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
