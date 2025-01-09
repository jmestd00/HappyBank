package org.HappyBank.controller.Client;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import org.HappyBank.model.*;
import org.HappyBank.view.ViewFactory;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Controlador para la vista principal de cliente.
 */
public class ClientMainWindowController {

    private ViewFactory viewFactory = ViewFactory.getInstance(null);
    private String username;
    private String NIF;
    private Client client;
    @FXML
    private Label welcomeLabel;
    @FXML
    private Label accountNumber;
    @FXML
    private Label balanceText;
    @FXML
    private Label cardNumber;
    @FXML
    private Label cvv;
    @FXML
    private Label date;
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
    private BankService bankService = new BankService();
    private ArrayList<Transaction> transactions = new ArrayList<>();

    /**
     * Método que inicializa los datos de la tabla de los últimos movimientos.
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


    }

    /**
     * Método que inicializa los datos de la lista de movimientos.
     */
    private void setupData() {
        transactions = bankService.getLastTransactions(bankService.getAccount(client), 4);
        transactionTable.setItems(FXCollections.observableArrayList(transactions));
    }

    /**
     * Método que establece el NIF del cliente.
     * @param NIF (El NIF del cliente)
     */
    public void setNIF(String NIF) {
        this.NIF = NIF;
        this.client = bankService.getClient(NIF);
        this.username = client.getName() + " " + client.getSurname();
        welcomeLabel.setText("¡Hola " + username + "!\nBienvenido al panel de \nclientes de HappyBank.");
        CreditCard creditCard = bankService.getCreditCard(bankService.getAccount(client));
        cvv.setText("CVV: " + creditCard.getCVV());
        cardNumber.setText(creditCard.getNumber());
        date.setText("Valida hasta:\n" + creditCard.getExpirationDate().format(DateTimeFormatter.ofPattern("MM/yy")));
        balanceText.setText(bankService.getAccount(client).getBalance().toString() + " €");
        accountNumber.setText("Cuenta Bancaria: \n" + bankService.getAccount(client).getIBAN());
        setup();
    }

    /**
     * Método que cierra la sesión del cliente.
     */
    public void closeSession() {
        viewFactory.closeConcept();
        viewFactory.showCloseSessionConfirmation();
    }

    /**
     * Método que muestra la leyenda de la vista.
     */
    public void showLegend() {
        viewFactory.closeLegend();
        viewFactory.closeConcept();
        viewFactory.showClientLegend();
    }

    /**
     * Método que muestra la ventana de los datos personales del cliente.
     */
    public void showPersonalData() {
        viewFactory.closeConcept();
        viewFactory.showPersonalData(client);
    }

    /**
     * Método que muestra la ventana de realizar una transacción.
     */
    public void showPerformTransaction() {
        viewFactory.closeConcept();
        viewFactory.showPerformTransactionWindow(client, bankService.getAccount(client));
    }

    /**
     * Método que muestra la ventana de la lista de transacciones.
     */
    public void showTransactionList() {
        viewFactory.closeConcept();
        viewFactory.showTransactionList(client);
    }

}
