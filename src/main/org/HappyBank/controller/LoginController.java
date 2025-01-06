package org.HappyBank.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.HappyBank.model.*;
import org.HappyBank.view.ViewFactory;
import static org.HappyBank.model.DatabaseManager.*;

public class LoginController {
    @FXML
    private TextField userField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ComboBox accountPicker;
    private String username;
    private String password;
    private String accountType;
    private ViewFactory viewFactory = ViewFactory.getInstance(null);
    private boolean accSelected = false;

    /**
     * Initializes the login window.
     */
    public void initialize() {
        userField.setContextMenu(new ContextMenu());
        passwordField.setContextMenu(new ContextMenu());
        accountPicker.setValue("CLIENTE");
    }

    /**
     * Method that gets the value of the account picker, the NIF and the password.
     */
    private void getFields() {
        username = userField.getText();
        password = passwordField.getText();
        accountType = accountPicker.getValue().toString();
    }

    /**
     * Method that logis the user in using a query to the database to perform the login.
     * It throws a window with an error message if the login fails.
     */
    @FXML
    public void loginUser() {
        try {
            getInstance();
        } catch (HappyBankException e) {
            e.printStackTrace();
        }
        getFields();
        boolean success = false;
            if (accountType.equals("ADMINISTRADOR")) {
                success = false;
                try {
                    success = loginAdministrator(username, password);
                } catch (HappyBankException e) {
                    e.printStackTrace();
                }
                if (success) {
                    viewFactory.showAdminMainWindow(username);
                } else {
                    viewFactory.showError(new FXMLLoader(getClass().getResource("/fxml/Error/loginError.fxml")));
                }

            } else {
                success = false;
                try {
                    success = loginClient(username, password);
                } catch (HappyBankException e) {
                    e.printStackTrace();
                }
                if (success) {
                    viewFactory.showClientMainWindow(username);
                } else {
                    viewFactory.showError(new FXMLLoader(getClass().getResource("/fxml/Error/loginError.fxml")));
                }
            }
    }
}
