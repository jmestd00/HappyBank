package org.HappyBank.controller.Client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.HappyBank.model.Account;
import org.HappyBank.model.BankService;
import org.HappyBank.model.Client;
import org.HappyBank.view.ViewFactory;
import java.math.BigDecimal;

/**
 * Controlador para la vista de realizar transacción de cliente.
 */
public class ClientPerformTransactionController {
    private Account account;
    private Client client;
    private String username;
    private ViewFactory viewFactory;
    @FXML
    private Label welcomeLabel;
    @FXML
    private TextField amount;
    @FXML
    private TextField accNumber;
    @FXML
    private TextArea concept;
    private BankService bankService = new BankService();

    /**
     * Método que inicializa la vista de realizar transacción de cliente.
     */
    public void initialize() {
        viewFactory = ViewFactory.getInstance(null);
        amount.setContextMenu(new ContextMenu());
        accNumber.setContextMenu(new ContextMenu());
        concept.setContextMenu(new ContextMenu());

    }

    /**
     * Método que establece los datos del cliente y la cuenta en la vista.
     * @param client (El cliente que realiza la consulta)
     * @param account (La cuenta del cliente)
     */
    public void setData(Client client, Account account) {
        this.client = client;
        this.account = account;
        this.username = client.getName() + " " + client.getSurname();
        welcomeLabel.setText("¡Hola " + username + "!\nBienvenido al panel de \nclientes de HappyBank.");
    }

    /**
     * Método que cierra la sesión del cliente.
     */
    public void closeSession() {
        if (criticWindow()) {
            viewFactory.showError(new FXMLLoader(getClass().getResource("/fxml/Error/notEmptyPerformTransactionFields.fxml")));
        } else {
        viewFactory.showCloseSessionConfirmation();
        }
    }

    /**
     * Método que muestra la ventana de inicio.
     */
    public void goBack() {
        if (criticWindow()) {
            viewFactory.showError(new FXMLLoader(getClass().getResource("/fxml/Error/notEmptyPerformTransactionFields.fxml")));
        } else {
            viewFactory.showClientMainWindow(client.getNIF());
        }
    }

    /**
     * Método que muestra la leyenda de la vista.
     */
    public void showLegend() {
        viewFactory.closeLegend();
        viewFactory.showClientLegend();
    }

    /**
     * Método que realiza la transacción de la cuenta del cliente.
     */
    public void performTransaction() {
        try {
            if (account.equals(bankService.getAccount(accNumber.getText()))) {
                viewFactory.showError(new FXMLLoader(getClass().getResource("/fxml/Error/receiverAccountCanNotBeTheSame.fxml")));
            }  else if (account.getBalance().compareTo(new BigDecimal(amount.getText())) < 0) {
                viewFactory.showError(new FXMLLoader(getClass().getResource("/fxml/Error/insufficientFunds.fxml")));

            } else {

                if (concept.getText().isEmpty()) {
                    concept.setText("Sin concepto");
                }
                if (!amount.getText().isEmpty() && !accNumber.getText().isEmpty()) {
                        bankService.createTransaction(account, bankService.getAccount(accNumber.getText()), concept.getText(), new BigDecimal(amount.getText()));
                        this.account.setBalance(this.account.getBalance().subtract(new BigDecimal(amount.getText())));
                        Account receiver = bankService.getAccount(accNumber.getText());
                        receiver.setBalance(receiver.getBalance().add(new BigDecimal(amount.getText())));
                        accNumber.clear();
                        amount.clear();
                        concept.clear();
                } else {
                    viewFactory.showError(new FXMLLoader(getClass().getResource("/fxml/Error/emptyPerformTransactionFields.fxml")));
                }
            }
        } catch (RuntimeException e) {
            viewFactory.showError(new FXMLLoader(getClass().getResource("/fxml/Error/receiverAccountDoesNotExists.fxml")));
        }
    }

    /**
     * Método que comprueba si los campos de la ventana están vacíos.
     * @return (Devuelve true si los campos están vacíos, false si no)
     */
    private boolean criticWindow() {
        if (amount.getText().isEmpty() && accNumber.getText().isEmpty() && concept.getText().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

}
