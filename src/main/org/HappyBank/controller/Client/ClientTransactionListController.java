package org.HappyBank.controller.Client;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import org.HappyBank.model.Client;
import org.HappyBank.view.ViewFactory;
import org.HappyBank.model.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ClientTransactionListController {
    private ViewFactory viewFactory;
    private Client client;
    private String username;
    @FXML
    private Label welcomeLabel;
    ArrayList<Transaction> transactions = new ArrayList<>();
    private final int ROWS_PER_PAGE = 7;
    private BankService bankService = new BankService();
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
    private TableColumn<Transaction, Void> conceptCol;
    @FXML
    private Pagination pagination;

    public void initialize() {
        viewFactory = ViewFactory.getInstance(null);
    }

    /**
     * Initializes the viewFactory instance, the bbdd instance, the transaction list and the pagination.
     */
    public void setup() {

        setupData();
        transactionTable.setSelectionModel(null);

        dateCol.setReorderable(false);
        senderCol.setReorderable(false);
        receiverCol.setReorderable(false);
        amountCol.setReorderable(false);
        conceptCol.setReorderable(false);
        dateCol.setCellValueFactory(data ->  {
            if (data.getValue() == null) {
                return new SimpleStringProperty("");
            } else {
                return new SimpleStringProperty(data.getValue().getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")).toString());
            }
        });
        senderCol.setCellValueFactory(data -> {
            if (data.getValue() == null) {
                return new SimpleStringProperty("");
            } else {
                return new SimpleStringProperty(data.getValue().getSender().getOwner().getName() + " " + data.getValue().getSender().getOwner().getSurname());
            }
        });
        receiverCol.setCellValueFactory(data -> {
            if (data.getValue() == null) {
                return new SimpleStringProperty("");
            } else {
                return new SimpleStringProperty(data.getValue().getReceiver().getOwner().getName() + " " + data.getValue().getReceiver().getOwner().getSurname());
            }
        });
        amountCol.setCellValueFactory(data -> {
            if (data.getValue() == null) {
                return new SimpleStringProperty("");
            } else {
                if (data.getValue().getSender().getOwner().getNIF().equals(this.client.getNIF())) {
                    return new SimpleStringProperty("- " + data.getValue().getAmount().toString() + " €");
                } else {
                    return new SimpleStringProperty("+ " + data.getValue().getAmount().toString() + " €");
                }
            }
        });
        conceptCol.setCellFactory(param -> new TableCell<Transaction, Void>() {
            private final Button concept = new Button("");
            {
                concept.setOnAction(event -> {
                    Transaction selectedTransaction = getTableView().getItems().get(getIndex());
                    if (selectedTransaction != null) {
                        Bounds boundsInScreen = concept.localToScreen(concept.getBoundsInLocal());
                        // Mover la ventana 40 píxeles por encima de la esquina superior izquierda del botón
                        double newX = boundsInScreen.getMinX() - 151;
                        double newY = boundsInScreen.getMinY() - 90;
                        viewFactory.closeConcept();
                        Point2D coords = new Point2D(newX, newY);
                        viewFactory.showConcept(selectedTransaction.getConcept(), getIndex(), coords);
                    }
                });

                concept.getStylesheets().add(getClass().getResource("/css/Admin/AdminClientTransactionHistory.css").toString());
                concept.getStyleClass().add("conceptButton");
                concept.setPrefHeight(53);
                concept.setPrefWidth(53);

            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    Transaction selectedTransaction = getTableView().getItems().get(getIndex());
                    if (selectedTransaction != null) {
                        setGraphic(concept);
                        setAlignment(Pos.CENTER);
                    } else {
                        setGraphic(null);
                    }
                }
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
        transactions = bankService.getAccountTransactions(bankService.getAccount(client));
    }

    public void setData(Client client) {
        this.client = client;
        this.username = client.getName() + " " + client.getSurname();
        welcomeLabel.setText("¡Hola " + username + "!\nBienvenido al panel de \nclientes de HappyBank.");
        setup();
    }

    public void closeSession() {
        viewFactory.closeConcept();
        viewFactory.showCloseSessionConfirmation();
    }

    public void showLegend() {
        viewFactory.closeConcept();
        viewFactory.closeLegend();
        viewFactory.showClientLegend();
    }

    public void goBack() {
        viewFactory.closeConcept();
        viewFactory.showClientMainWindow(client.getNIF());
    }

}
