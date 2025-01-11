package org.HappyBank.model;

import org.HappyBank.model.repository.*;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Clase que representa el servicio de acceso a las operaciones en base de datos de manera simplificada.
 */
public class BankService {
    private AccountRepositoryImpl accountRepository;
    private AdministratorRepositoryImpl administratorRepository;
    private ClientRepositoryImpl clientRepository;
    private CreditCardRepositoryImpl cardRepository;
    private TransactionRepositoryImpl transactionRepository;
    
    
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
    
    //Setters
    /**
     * Establece el repositorio de cuentas.
     *
     * @param accountRepository Repositorio de cuentas.
     */
    public void setAccountRepository(AccountRepositoryImpl accountRepository) {
        this.accountRepository = accountRepository;
    }
    
    /**
     * Establece el repositorio de administradores.
     *
     * @param administratorRepository Repositorio de administradores.
     */
    public void setAdministratorRepository(AdministratorRepositoryImpl administratorRepository) {
        this.administratorRepository = administratorRepository;
    }
    
    /**
     * Establece el repositorio de clientes.
     *
     * @param clientRepository Repositorio de clientes.
     */
    public void setClientRepository(ClientRepositoryImpl clientRepository) {
        this.clientRepository = clientRepository;
    }
    
    /**
     * Establece el repositorio de tarjetas de crédito.
     *
     * @param cardRepository Repositorio de tarjetas de crédito.
     */
    public void setCreditCardRepository(CreditCardRepositoryImpl cardRepository) {
        this.cardRepository = cardRepository;
    }
    
    /**
     * Establece el repositorio de transacciones.
     *
     * @param transactionRepository Repositorio de transacciones.
     */
    public void setTransactionRepository(TransactionRepositoryImpl transactionRepository) {
        this.transactionRepository = transactionRepository;
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
     */
    public void createClient(String name, String surname, String NIF, String email, String phone, String address, String bank, String password) {
        Client client = new Client(name, surname, NIF, email, phone, address, bank);
        clientRepository.add(client);
        clientRepository.changePassword(NIF, password);
        createAccount(client);
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
    public void removeClient(Client client) {
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
     */
    public void createAdministrator(String name, String surname, String NIF, String SSN, BigDecimal salary, String bank, String password) {
        administratorRepository.add(new Administrator(name, surname, NIF, SSN, salary, bank));
        administratorRepository.changePassword(NIF, password);
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
     * @param client Propietario de la cuenta.
     */
    public void createAccount(Client client) {
        Account account = new Account(client);
        accountRepository.add(account);
        createCreditCard(account);
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
     * @param account Cuenta asociada a la tarjeta.
     */
    public void createCreditCard(Account account) {
        cardRepository.add(new CreditCard(account));
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
     */
    public void createTransaction(Account sender, Account receiver, String concept, BigDecimal amount) throws HappyBankException {
        transactionRepository.add(new Transaction(sender, receiver, concept, amount));
    }
    
    /**
     * Obtiene todas las transacciones.
     *
     * @return Lista de todas las transacciones.
     */
    public ArrayList<Transaction> getAllTransactions() {
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
     * Obtiene las últimas X transacciones.
     *
     * @param account  Cuenta de la que se quieren obtener las transacciones.
     * @param quantity Cantidad de transacciones a obtener.
     * @return Lista de las últimas X transacciones.
     */
    public ArrayList<Transaction> getLastTransactions(Account account, int quantity) {
        return transactionRepository.getLastTransactions(account, quantity);
    }
}
