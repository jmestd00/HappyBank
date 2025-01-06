package org.HappyBank.controller.Admin;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.HappyBank.model.Administrator;
import org.HappyBank.model.Client;
import org.HappyBank.model.HappyBankException;
import org.HappyBank.model.Transaction;
import org.HappyBank.view.ViewFactory;


import static org.HappyBank.model.DatabaseManager.*;

import java.util.ArrayList;

/**
 * Controller for the client transaction history view.
 */
public class AdminClientTransactionHistoryController {
    private Client client;
    private Administrator admin;
    private ViewFactory viewFactory;
    @FXML
    private Label welcomeLabel;
    @FXML
    private Label clientLabel;
    private String username;
    @FXML
    private TableView<Transaction> transactionTable;
    @FXML
    private TableColumn<Transaction, String> dateCol;
    @FXML
    private TableColumn<Transaction, String> senderCol;
    @FXML
    private TableColumn<Transaction, String> receiverCol;
    @FXML
    private TableColumn<Transaction, String> amountCol;
    @FXML
    private Pagination pagination;
    private final int ROWS_PER_PAGE = 7;
    private ArrayList<Transaction> transactions = new ArrayList<>();


    /**
     * Initializes the viewFactory instance, the bbdd instance, the transaction list and the pagination.
     */
    public void initialize() {
        try {
            getInstance();
        } catch(HappyBankException e) {
            e.printStackTrace();
        }
        viewFactory = viewFactory.getInstance(null);
        setupData();
        transactionTable.setSelectionModel(null);

        dateCol.setReorderable(false);
        senderCol.setReorderable(false);
        receiverCol.setReorderable(false);
        amountCol.setReorderable(false);
        dateCol.setCellValueFactory(data ->  {
            if (data.getValue() == null) {
                return new SimpleStringProperty("");
            } else {
                return data.getValue().getProperties()[4];
            }
        });
        senderCol.setCellValueFactory(data -> {
            if (data.getValue() == null) {
                return new SimpleStringProperty("");
            } else {
                return data.getValue().getProperties()[0];
            }
        });
        receiverCol.setCellValueFactory(data -> {
            if (data.getValue() == null) {
                return new SimpleStringProperty("");
            } else {
                return data.getValue().getProperties()[1];
            }
        });
        amountCol.setCellValueFactory(data -> {
            if (data.getValue() == null) {
                return new SimpleStringProperty("");
            } else {
                return data.getValue().getProperties()[3];
            }
        });
        int totalPage = (int) (Math.ceil(transactions.size() * 1.0 / ROWS_PER_PAGE));
        pagination.setPageCount(totalPage);
        pagination.setCurrentPageIndex(0);
        changeTableView(0, ROWS_PER_PAGE);
        pagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            changeTableView(newValue.intValue(), ROWS_PER_PAGE);
        });

    }

    /**
     * Method that changes the table view.
     * @param index
     * @param limit
     */
    private void changeTableView(int index, int limit) {
        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, transactions.size());
        int minIndex = Math.min(toIndex, transactions.size());

        ObservableList<Transaction> pageData = FXCollections.observableArrayList(transactions.subList(fromIndex, minIndex));

        int remainingRows = limit - pageData.size();
        for (int i = 0; i < remainingRows; i++) {
            pageData.add(null);
        }

        SortedList<Transaction> sortedData = new SortedList<>(pageData);
        sortedData.comparatorProperty().bind(transactionTable.comparatorProperty());

        transactionTable.setItems(sortedData);
    }

    /**
     * Method that sets up the data of the transaction list.
     */
    private void setupData() {
/*        try {
        //transactions = getAllTransactions(client.getNIF());
        } catch (HappyBankException e) {
            e.printStackTrace();
        }*/
    }

    /**
     * Method that sets the data of the client and the administrator.
     * @param client
     * @param administrator
     */
    public void setData(Client client, Administrator administrator) {
        this.client = client;
        this.admin = administrator;
        username = this.admin.getName() + " " + this.admin.getSurname();
        welcomeLabel.setText("¡Hola " + username + "!\nBienvenido al panel de \nadministración de HappyBank.");
        clientLabel.setText("Cliente: " + client.getName() + " " + client.getSurname());
    }

    /**
     * Method that goes back to the client list view.
     */
    public void goBack() {
        viewFactory.showClientList(admin);
    }

    /**
     * Method that closes the session.
     */
    public void closeSession () {
        viewFactory.showLoginView();
    }

    /**
     * Method that goes to the main window.
     */
    public void goMain () {
        viewFactory.showAdminMainWindow(admin.getNIF());
    }
}
