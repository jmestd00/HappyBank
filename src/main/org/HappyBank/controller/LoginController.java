package org.HappyBank.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.HappyBank.model.*;
import org.HappyBank.view.ViewFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Controlador para la vista de inicio de sesión.
 */
public class LoginController {
private static final Logger logger = LogManager.getLogger(LoginController.class.getName());
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
    private BankService bankService = new BankService();

    /**
     * Método que se encarga de inicializar la vista de inicio de sesión.
     */
    public void initialize() {
        userField.setContextMenu(new ContextMenu());
        passwordField.setContextMenu(new ContextMenu());
        accountPicker.setValue("CLIENTE");
    }

    /**
     * Método que se encarga de obtener los campos de texto de la vista de inicio de sesión.
     */
    private void getFields() {
        username = userField.getText();
        password = passwordField.getText();
        accountType = accountPicker.getValue().toString();
    }

    /**
     * Método que inicia sesión del usuario utilizando una consulta a la base de datos para realizar el inicio de sesión.
     * Muestra una ventana con un mensaje de error si el inicio de sesión falla.
     */
    @FXML
    public void loginUser() {

        getFields();
        boolean success = false;
            if (accountType.equals("ADMINISTRADOR")) {
                success = false;
                success = bankService.loginAdministrator(username, password);
                if (success) {
                    viewFactory.showAdminMainWindow(username);
                } else {
                    viewFactory.showError(new FXMLLoader(getClass().getResource("/fxml/Error/loginError.fxml")));
                    logger.error("Error al iniciar sesión como administrador, contraseña o usuario incorrectos.");
                }

            } else {
                success = false;
                    success = bankService.loginClient(username, password);
                if (success) {
                    viewFactory.showClientMainWindow(username);
                } else {
                    viewFactory.showError(new FXMLLoader(getClass().getResource("/fxml/Error/loginError.fxml")));
                    logger.error("Error al iniciar sesión como cliente, contraseña o usuario incorrectos.");
                }
            }
    }
}
