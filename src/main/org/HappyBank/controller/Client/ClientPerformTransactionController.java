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
import org.HappyBank.model.HappyBankException;
import org.HappyBank.view.ViewFactory;
import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Controlador para la vista de realizar transacción de cliente.
 */
public class ClientPerformTransactionController {
private static final Logger logger = LogManager.getLogger(ClientPerformTransactionController.class.getName());
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
            viewFactory.showError(new FXMLLoader(getClass().getResource("/fxml/Error/NotEmptyPerformTransactionFields.fxml")));
            logger.error("No se puede cerrar sesión porque los campos de transacción no están vacíos.");
        } else {
        viewFactory.showCloseSessionConfirmation();
        }
    }

    /**
     * Método que muestra la ventana de inicio.
     */
    public void goBack() {
        if (criticWindow()) {
            viewFactory.showError(new FXMLLoader(getClass().getResource("/fxml/Error/NotEmptyPerformTransactionFields.fxml")));
            logger.error("No se puede volver a la ventana anterior porque los campos de transacción no están vacíos.");
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
            if (account.getBalance().compareTo(new BigDecimal(amount.getText())) < 0) {
                viewFactory.showError(new FXMLLoader(getClass().getResource("/fxml/Error/InsufficientFunds.fxml")));
                logger.error("El cliente no tiene fondos suficientes en la cuenta para realicar la transacción de " + amount.getText() + "€.");
            } else {
                    if (concept.getText().isEmpty()) {
                        concept.setText("Sin concepto");
                    }
                    if (!amount.getText().isEmpty() && !accNumber.getText().isEmpty()) {
                        logger.info("El cliente " + client.getNIF() + " está realizando una transacción.");
                        bankService.createTransaction(account, bankService.getAccount(accNumber.getText()), concept.getText(), new BigDecimal(amount.getText()));
                        this.account.setBalance(this.account.getBalance().subtract(new BigDecimal(amount.getText())));
                        Account receiver = bankService.getAccount(accNumber.getText());
                        receiver.setBalance(receiver.getBalance().add(new BigDecimal(amount.getText())));
                        accNumber.clear();
                        amount.clear();
                        concept.clear();
                    } else {
                        viewFactory.showError(new FXMLLoader(getClass().getResource("/fxml/Error/EmptyPerformTransactionFields.fxml")));
                        logger.error("No se puede realizar la transacción porque los campos requeridos no están completos.");
                    }
                }
        } catch (RuntimeException e) {
            viewFactory.showError(new FXMLLoader(getClass().getResource("/fxml/Error/ReceiverAccountDoesNotExists.fxml")));
            logger.error("No se puede realizar la transacción porque la cuenta de destino no existe.");
        } catch (HappyBankException e) {
                        viewFactory.showError(new FXMLLoader(getClass().getResource("/fxml/Error/ReceiverAccountCanNotBeTheSame.fxml")));
                        logger.error("No se puede realizar la transacción porque la cuenta de destino no puede ser la misma que la de origen.");
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
