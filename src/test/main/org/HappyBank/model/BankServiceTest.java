package org.HappyBank.model;

import org.HappyBank.model.repository.*;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BankServiceTest {
    private BankService bankService;
    private Account mockAccount;
    private Administrator mockAdmin;
    private Client mockClient;
    private CreditCard mockCard;
    private Transaction mockTransaction;
    
    @Before
    public void setUp() throws Exception {
        //Creo el spy de la clase y los objetos con los que testearé
        bankService = spy(BankService.class);
        mockAccount = new Account("ES19 0064 0001 83 9329930006", new Client("Juan", "Pérez", "12345678A", "juanperez@example.com", "123456789", "Calle Falsa 123", "HappyBank"), new BigDecimal(100));
        mockClient = new Client("12345678A", "Juan", "Pérez", "juanperez@example.com", "123456789", "Calle Falsa 123", "HappyBank");
        mockAdmin = new Administrator("Juan", "Pérez", "12345678A", "1234567890", new BigDecimal(1200), "HappyBank");
        mockCard = new CreditCard("6440 9643 8090 5200", mockAccount, LocalDate.of(2027, 4, 17), "123");
        mockTransaction = new Transaction(mockAccount, new Account("ES07 0064 0001 60 9168908623", new Client("Pedro", "García", "87654321Z", "pedrogarcia@example.com", "987654321", "Calle Irreal 321", "HappyBank"), new BigDecimal(100)), "Concepto", new BigDecimal(100), LocalDateTime.of(2025, 1, 1, 12, 0));
        
        //Creo los repositorios mockeados
        AccountRepositoryImpl accountRepository = mock(AccountRepositoryImpl.class);
        AdministratorRepositoryImpl administratorRepository = mock(AdministratorRepositoryImpl.class);
        ClientRepositoryImpl clientRepository = mock(ClientRepositoryImpl.class);
        CreditCardRepositoryImpl cardRepository = mock(CreditCardRepositoryImpl.class);
        TransactionRepositoryImpl transactionRepository = mock(TransactionRepositoryImpl.class);
        
        //Setteo los repositorios mockeados en el servicio
        bankService.setAccountRepository(accountRepository);
        bankService.setAdministratorRepository(administratorRepository);
        bankService.setClientRepository(clientRepository);
        bankService.setCreditCardRepository(cardRepository);
        bankService.setTransactionRepository(transactionRepository);
        
        //Hago que los métodos get de los repositorios mockeados devuelvan los objetos mockeados
        doReturn(mockClient).when(clientRepository).get(any());
        doReturn(mockAdmin).when(administratorRepository).get(any());
        doReturn(mockAccount).when(accountRepository).get((String) any());
        doReturn(mockAccount).when(accountRepository).get((Client) any());
        doReturn(mockCard).when(cardRepository).get((String) any());
        doReturn(mockCard).when(cardRepository).get((Account) any());
        
        //Hago que el login siempre devuelva true
        doReturn(true).when(clientRepository).validateClient(any(), any());
        doReturn(true).when(administratorRepository).validateAdministrator(any(), any());
        
        //Hago que los métodos changePassword no haga nada
        doNothing().when(clientRepository).changePassword(any(), any());
        doNothing().when(administratorRepository).changePassword(any(), any());
        
        //Hago que los métodos remove no haga nada
        doNothing().when(clientRepository).remove(any());
        doNothing().when(administratorRepository).remove(any());
        doNothing().when(accountRepository).remove(any());
        doNothing().when(cardRepository).remove(any());
        
        //Hago que los métodos getAll devuelvan una lista con los objetos mockeados
        doReturn(new ArrayList<Client>(){{add(mockClient);}}).when(clientRepository).getAll();
        doReturn(new ArrayList<Client>(){{add(mockClient);}}).when(clientRepository).searchClient(any(), any(), any());
        doReturn(new ArrayList<Administrator>(){{add(mockAdmin);}}).when(administratorRepository).getAll();
        doReturn(new ArrayList<Account>(){{add(mockAccount);}}).when(accountRepository).getAll();
        doReturn(new ArrayList<CreditCard>(){{add(mockCard);}}).when(cardRepository).getAll();
        doReturn(new ArrayList<Transaction>(){{add(mockTransaction);}}).when(transactionRepository).getAll();
        
        //Hago que los métodos getAllTransactions devuelvan una lista con los objetos mockeados
        doReturn(new ArrayList<Transaction>(){{add(mockTransaction);}}).when(transactionRepository).getAccountTransactions(any());
    }
    
    @Test
    public void createClientTest() throws HappyBankException {
        bankService.createClient("Juan", "Perez", "12345678A", "juanperez@example.com", "123456789", "Calle Falsa 123", "HappyBank", "1234");
        bankService.createAdministrator("Juan", "Perez", "12345678A", "1234567890", new BigDecimal(1200), "HappyBank", "1234");
        bankService.createTransaction(mockAccount, new Account("ES07 0064 0001 60 9168908623", new Client("Pedro", "García", "87654321Z", "pedrogarcia@example.com", "987654321", "Calle Irreal 321", "HappyBank"), new BigDecimal(100)), "Concepto", new BigDecimal(100));
    }
    
    @Test
    public void getTest() {
        Client gottenClient = bankService.getClient("12345678A");
        Administrator gottenAdmin = bankService.getAdministrator("12345678A");
        Account gottenAccountByIBAN = bankService.getAccount("ES19 0064 0001 83 9329930006");
        Account gottenAccountByClient = bankService.getAccount(mockClient);
        CreditCard gottenCardByNumber = bankService.getCreditCard("6440 9643 8090 5200");
        CreditCard gottenCardByAccount = bankService.getCreditCard(mockAccount);
        
        assertEquals(mockClient, gottenClient);
        assertEquals(mockAdmin, gottenAdmin);
        assertEquals(mockAccount, gottenAccountByIBAN);
        assertEquals(mockAccount, gottenAccountByClient);
        assertEquals(mockCard, gottenCardByNumber);
        assertEquals(mockCard, gottenCardByAccount);
    }
    
    @Test
    public void validateTest() {
        boolean clientValidation = bankService.loginClient("12345678A", "1234");
        boolean adminValidation = bankService.loginAdministrator("12345678A", "1234");
        
        assertTrue(clientValidation);
        assertTrue(adminValidation);
    }
    
    @Test
    public void changePasswordRepository() {
        bankService.changeClientPassword("12345678A", "1234");
        bankService.changeAdministratorPassword("12345678A", "1234");
    }
    
    @Test
    public void removeTest() {
        bankService.removeClient(mockClient);
        bankService.removeAdministrator(mockAdmin);
        bankService.removeAccount(mockAccount);
        bankService.removeCreditCard(mockCard);
    }
    
    @Test
    public void getAllTest() {
        ArrayList<Client> clients = bankService.getAllClients();
        ArrayList<Client> searchedClient = bankService.searchClients("Juan", "Pérez", "12345678A");
        ArrayList<Administrator> admins = bankService.getAllAdministrators();
        ArrayList<Account> accounts = bankService.getAllAccounts();
        ArrayList<CreditCard> cards = bankService.getAllCreditCards();
        ArrayList<Transaction> transactions = bankService.getAllTransactions();
        
        assertEquals(1, clients.size());
        assertEquals(1, searchedClient.size());
        assertEquals(1, admins.size());
        assertEquals(1, accounts.size());
        assertEquals(1, cards.size());
        assertEquals(1, transactions.size());
    }
    
    @Test
    public void getAccountTransactionsTest() {
        ArrayList<Transaction> transactions = bankService.getAccountTransactions(mockAccount);
        ArrayList<Transaction> lastTransactions = bankService.getLastTransactions(mockAccount, 0);
        
        assertEquals(1, transactions.size());
        assertEquals(0, lastTransactions.size());
    }
}
