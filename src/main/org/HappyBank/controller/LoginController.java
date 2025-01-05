package org.HappyBank.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.HappyBank.controller.Admin.AdminMainWindowController;
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

    public void initialize() {
        userField.setContextMenu(new ContextMenu());
        passwordField.setContextMenu(new ContextMenu());
        accountPicker.setValue("CLIENTE");
    }

    private void getFields() {
        username = userField.getText();
        password = passwordField.getText();
        accountType = accountPicker.getValue().toString();
    }

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
