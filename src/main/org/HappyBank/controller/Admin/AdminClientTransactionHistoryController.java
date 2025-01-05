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

    private void changeTableView(int index, int limit) {
        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, transactions.size());
        int minIndex = Math.min(toIndex, transactions.size());

        // Crea una lista de lotes para la página actual
        ObservableList<Transaction> pageData = FXCollections.observableArrayList(transactions.subList(fromIndex, minIndex));

        // Si hay espacio restante en la página, agrega filas vacías
        int remainingRows = limit - pageData.size();
        for (int i = 0; i < remainingRows; i++) {
            pageData.add(null);  // Añade una fila vacía representada por "null"
        }

        // Crear una SortedList para asegurar que se ordenen correctamente
        SortedList<Transaction> sortedData = new SortedList<>(pageData);
        sortedData.comparatorProperty().bind(transactionTable.comparatorProperty());

        transactionTable.setItems(sortedData);
    }

    public void refreshTable() {
        int totalPage = (int) Math.ceil(transactions.size() * 1.0 / ROWS_PER_PAGE);
        pagination.setPageCount(totalPage);

        // Recargar los datos de la tabla
        changeTableView(pagination.getCurrentPageIndex(), ROWS_PER_PAGE);
    }

    private void setupData() {
/*        try {
        //transactions = getAllTransactions(client.getNIF());
        } catch (HappyBankException e) {
            e.printStackTrace();
        }*/
    }

    public void setData(Client client, Administrator administrator) {
        this.client = client;
        this.admin = administrator;
        username = this.admin.getName() + " " + this.admin.getSurname();
        welcomeLabel.setText("¡Hola " + username + "!\nBienvenido al panel de \nadministración de HappyBank.");
        clientLabel.setText("Cliente: " + client.getName() + " " + client.getSurname());
    }

    public void goBack() {
        viewFactory.showClientList(admin);
    }
    public void closeSession () {
        viewFactory.showLoginView();
    }

    public void goMain () {
        viewFactory.showAdminMainWindow(admin.getNIF());
    }
}
