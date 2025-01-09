package org.HappyBank.model;

import org.HappyBank.model.repository.*;

import java.math.BigDecimal;
import java.util.ArrayList;

public class BankService {
    private final AccountRepositoryImpl accountRepository;
    private final AdministratorRepositoryImpl administratorRepository;
    private final ClientRepositoryImpl clientRepository;
    private final CreditCardRepositoryImpl cardRepository;
    private final TransactionRepositoryImpl transactionRepository;
    
    
    //Constructor
    /**
     * Constructor del servicio.
     */
    public BankService() {
        accountRepository = new AccountRepositoryImpl();
        administratorRepository = new AdministratorRepositoryImpl();
        clientRepository = new ClientRepositoryImpl();
        cardRepository = new CreditCardRepositoryImpl();
        transactionRepository = new TransactionRepositoryImpl();
    }
    
    
    //Client
    /**
     * Crea un cliente.
     *
     * @param name     Nombre del cliente.
     * @param surname  Apellidos del cliente.
     * @param NIF      NIF del cliente.
     * @param email    Email del cliente.
     * @param phone    Teléfono del cliente.
     * @param address  Dirección del cliente.
     * @param bank     Entidad bancaria del cliente.
     * @param password Contraseña del cliente.
     * @return Cliente creado.
     */
    public Client createClient(String name, String surname, String NIF, String email, String phone, String address, String bank, String password) {
        return new Client(name, surname, NIF, email, phone, address, bank, password);
    }
    
    /**
     * Obtiene un cliente.
     *
     * @param NIF NIF del cliente.
     * @return Cliente.
     */
    public Client getClient(String NIF) {
        return clientRepository.get(NIF);
    }
    
    /**
     * Busca clientes.
     *
     * @param NIF     NIF del cliente.
     * @param name    Nombre del cliente.
     * @param surname Apellidos del cliente.
     * @return Lista de clientes que cumplen los criterios de búsqueda.
     */
    public ArrayList<Client> searchClients(String NIF, String name, String surname) {
        return clientRepository.searchClient(NIF, name, surname);
    }
    
    /**
     * Obtiene todos los clientes.
     *
     * @return Lista de todos los clientes.
     */
    public ArrayList<Client> getAllClients() {
        return clientRepository.getAll();
    }
    
    /**
     * Elimina un cliente.
     *
     * @param client Cliente a eliminar.
     */
    public void deleteClient(Client client) {
        clientRepository.remove(client);
    }
    
    /**
     * Autentifica a un cliente del banco
     *
     * @param NIF      NIF del cliente
     * @param password Contraseña del cliente
     * @return True si sus credenciales son correctas, false en caso contrario
     */
    public boolean loginClient(String NIF, String password) {
        return clientRepository.validateClient(NIF, password);
    }
    
    /**
     * Cambia la contraseña de un cliente.
     *
     * @param NIF         NIF del cliente.
     * @param newPassword Nueva contraseña.
     */
    public void changeClientPassword(String NIF, String newPassword) {
        clientRepository.changePassword(NIF, newPassword);
    }
    
    
    //Admin
    /**
     * Crea un administrador.
     *
     * @param name     Nombre del administrador.
     * @param surname  Apellidos del administrador.
     * @param NIF      NIF del administrador.
     * @param SSN      SSN del administrador.
     * @param salary   Salario del administrador.
     * @param bank     Entidad bancaria del administrador.
     * @param password Contraseña del administrador.
     * @return Administrador creado.
     */
    public Administrator createAdministrator(String name, String surname, String NIF, String SSN, BigDecimal salary, String bank, String password) {
        return new Administrator(name, surname, NIF, SSN, salary, bank, password);
    }
    
    /**
     * Obtiene un administrador.
     *
     * @param NIF NIF del administrador.
     * @return Administrador.
     */
    public Administrator getAdministrator(String NIF) {
        return administratorRepository.get(NIF);
    }
    
    /**
     * Obtiene todos los administradores.
     *
     * @return Lista de todos los administradores.
     */
    public ArrayList<Administrator> getAllAdministrators() {
        return administratorRepository.getAll();
    }
    
    /**
     * Elimina un administrador.
     *
     * @param admin Administrador a eliminar.
     */
    public void removeAdministrator(Administrator admin) {
        administratorRepository.remove(admin);
    }
    
    /**
     * Autentifica a un administrador del banco
     *
     * @param NIF      NIF del cliente
     * @param password Contraseña del administrador
     * @return True si sus credenciales son correctas, false en caso contrario
     */
    public boolean loginAdministrator(String NIF, String password) {
        return administratorRepository.validateAdministrator(NIF, password);
    }
    
    /**
     * Cambia la contraseña de un administrador.
     *
     * @param NIF         NIF del administrador.
     * @param newPassword Nueva contraseña.
     */
    public void changeAdministratorPassword(String NIF, String newPassword) {
        administratorRepository.changePassword(NIF, newPassword);
    }
    
    
    //Account
    /**
     * Crea una cuenta.
     *
     * @param ownerNIF NIF del propietario de la cuenta.
     * @return Cuenta creada.
     */
    public Account createAccount(String ownerNIF) {
        return new Account(clientRepository.get(ownerNIF));
    }
    
    /**
     * Obtiene una cuenta.
     *
     * @param IBAN IBAN de la cuenta.
     * @return Cuenta.
     */
    public Account getAccount(String IBAN) {
        return accountRepository.get(IBAN);
    }
    
    /**
     * Obtiene una cuenta.
     *
     * @param client Cliente propietario de la cuenta.
     * @return Cuenta.
     */
    public Account getAccount(Client client) {
        return accountRepository.get(client);
    }
    
    /**
     * Obtiene todas las cuentas.
     *
     * @return Lista de todas las cuentas.
     */
    public ArrayList<Account> getAllAccounts() {
        return accountRepository.getAll();
    }
    
    /**
     * Elimina una cuenta.
     *
     * @param account Cuenta a eliminar.
     */
    public void removeAccount(Account account) {
        accountRepository.remove(account);
    }
    
    
    //CreditCard
    /**
     * Crea una tarjeta de crédito.
     *
     * @param IBAN IBAN de la cuenta asociada a la tarjeta.
     * @return Tarjeta de crédito creada.
     */
    public CreditCard createCreditCard(String IBAN) {
        return new CreditCard(accountRepository.get(IBAN));
    }
    
    /**
     * Obtiene una tarjeta de crédito.
     *
     * @param number Número de la tarjeta de crédito.
     * @return Tarjeta de crédito.
     */
    public CreditCard getCreditCard(String number) {
        return cardRepository.get(number);
    }
    
    /**
     * Obtiene una tarjeta de crédito.
     *
     * @param account Cuenta asociada a la tarjeta de crédito.
     * @return Tarjeta de crédito.
     */
    public CreditCard getCreditCard(Account account) {
        return cardRepository.get(account);
    }
    
    /**
     * Obtiene todas las tarjetas de crédito.
     *
     * @return Lista de todas las tarjetas de crédito.
     */
    public ArrayList<CreditCard> getAllCreditCards() {
        return cardRepository.getAll();
    }
    
    /**
     * Elimina una tarjeta de crédito.
     *
     * @param card Tarjeta de crédito a eliminar.
     */
    public void removeCreditCard(CreditCard card) {
        cardRepository.remove(card);
    }
    
    
    //Transaction
    /**
     * Crea una transacción.
     *
     * @param sender   Cuenta emisora de la transacción.
     * @param receiver Cuenta receptora de la transacción.
     * @param concept  Concepto de la transacción.
     * @param amount   Cantidad de la transacción
     * @return Transacción creada.
     */
    public Transaction createTransaction(Account sender, Account receiver, String concept, BigDecimal amount) {
        return new Transaction(sender, receiver, concept, amount);
    }
    
    /**
     * Obtiene todas las transacciones.
     *
     * @return Lista de todas las transacciones.
     */
    public ArrayList<Transaction> getAllTransaction() {
        return transactionRepository.getAll();
    }
    
    /**
     * Obtiene las transacciones de una cuenta.
     *
     * @param account Cuenta de la que se quieren obtener las transacciones.
     * @return Lista con las transacciones de la cuenta.
     */
    public ArrayList<Transaction> getAccountTransactions(Account account) {
        return transactionRepository.getAccountTransactions(account);
    }
    
    /**
     * Obtiene las últimas n transacciones.
     *
     * @param quantity Cantidad de transacciones a obtener.
     * @return Lista de las últimas n transacciones.
     */
    public ArrayList<Transaction> getLastTransactions(Account account, int quantity) {
        return transactionRepository.getLastTransactions(account, quantity);
    }
}
