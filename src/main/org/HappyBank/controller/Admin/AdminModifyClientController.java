package org.HappyBank.controller.Admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.HappyBank.model.Administrator;
import org.HappyBank.model.*;
import org.HappyBank.view.ViewFactory;

/**
 * Controller for the modify client view.
 */
public class AdminModifyClientController {
    private Client client;
    private Administrator admin;
    @FXML
    private Label welcomeLabel;
    @FXML
    private TextField phone;
    @FXML
    private TextField email;
    @FXML
    private TextField address;
    @FXML
    private TextField password;
    @FXML
    private Label clientLabel;
    private String username;
    private ViewFactory viewFactory;
    private BankService bankService = new BankService();

    /**
     * Initializes the viewFactory instance.
     */
    public void initialize() {
        viewFactory = viewFactory.getInstance(null);
    }

    /**
     * Sets the data of the client and the administrator.
     * This method is called from the viewFactory instance.
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
     * Method that opens the confirmation window to delete the user.
     */
    public void deleteUser() {
        viewFactory.showConfirmationWindow(client, admin);
    }

    /**
     * Method that modifies the client data.
     */
    public void modifyClient() {
        if (!phone.getText().isEmpty()) {
            client.setPhone(phone.getText());
            phone.clear();
        }
        if (!email.getText().isEmpty()) {
            client.setEmail(email.getText());
            email.clear();
        }
        if (!address.getText().isEmpty()) {
            client.setAddress(address.getText());
            address.clear();
        }
        if (!password.getText().isEmpty()) {
            bankService.changeClientPassword(client.getNIF(), password.getText());
            password.clear();
        }
    }

    /**
     * Method that goes back to the client list view.
     * It shows a window with an error message if there are fields with data.
     */
    public void goBack() {
        if (checkCritic()) {
            viewFactory.showError(new FXMLLoader(getClass().getResource("/fxml/Error/notEmptyModifyPersonFields.fxml")));
        } else {
            viewFactory.showClientList(admin);
        }
    }

    /**
     * Method that closes the session.
     * It shows a window with an error message if there are fields with data.
     */
    public void closeSession() {
        if (checkCritic()) {
            viewFactory.showError(new FXMLLoader(getClass().getResource("/fxml/Error/notEmptyModifyPersonFields.fxml")));
        } else {
            viewFactory.showCloseSessionConfirmation();
        }
    }

    /**
     * Method that goes to the main window.
     * It shows a window with an error message if there are fields with data.
     */
    public void showLegend() {
        viewFactory.closeLegend();
        viewFactory.showAdminLegend();
    }

    /**
     * Method that checks if there are fields with data.
     * It is useful to check if the user can leave that window on the current state.
     * @return
     */
    private boolean checkCritic() {
        if (!phone.getText().isEmpty() || !email.getText().isEmpty() || !address.getText().isEmpty() || !password.getText().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }



}
