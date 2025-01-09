package org.HappyBank.controller.Client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.HappyBank.model.Account;
import org.HappyBank.model.BankService;
import org.HappyBank.model.Client;
import org.HappyBank.model.Transaction;
import org.HappyBank.view.ViewFactory;

import java.math.BigDecimal;

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

    public void initialize() {
        viewFactory = ViewFactory.getInstance(null);

    }

    public void setData(Client client, Account account) {
        this.client = client;
        this.account = account;
        this.username = client.getName() + " " + client.getSurname();
        welcomeLabel.setText("¡Hola " + username + "!\nBienvenido al panel de \nclientes de HappyBank.");
    }

    public void closeSession() {
        if (criticWindow()) {
            viewFactory.showError(new FXMLLoader(getClass().getResource("/fxml/Error/notEmptyPerformTransactionFields.fxml")));
        } else {
        viewFactory.showCloseSessionConfirmation();
        }
    }

    public void goBack() {
        if (criticWindow()) {
            viewFactory.showError(new FXMLLoader(getClass().getResource("/fxml/Error/notEmptyPerformTransactionFields.fxml")));
        } else {
            viewFactory.showClientMainWindow(client.getNIF());
        }
    }

    public void showLegend() {
        viewFactory.closeLegend();
        viewFactory.showClientLegend();
    }

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

    private boolean criticWindow() {
        if (amount.getText().isEmpty() && accNumber.getText().isEmpty() && concept.getText().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

}
