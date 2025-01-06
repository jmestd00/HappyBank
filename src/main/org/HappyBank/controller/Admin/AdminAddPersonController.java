package org.HappyBank.controller.Admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.HappyBank.model.*;
import org.HappyBank.view.ViewFactory;

import java.math.BigDecimal;

import static org.HappyBank.model.DatabaseManager.*;

/**
 * Controller for the add person view.
 */
public class AdminAddPersonController {
    private Administrator administrator;
    private ViewFactory viewFactory;
    private String username;
    @FXML
    private ComboBox<String> type;
    @FXML
    private VBox Admin;
    @FXML
    private VBox Client;
    @FXML
    private Label welcomeLabel;
    /* ADMIN */
    @FXML
    private TextField adminName;
    @FXML
    private TextField adminSurname;
    @FXML
    private TextField adminNIF;
    @FXML
    private TextField adminSSN;
    @FXML
    private TextField adminSalary;
    @FXML
    private TextField adminPassword;
    /* CLIENT */
    @FXML
    private TextField clientName;
    @FXML
    private TextField clientSurname;
    @FXML
    private TextField clientNIF;
    @FXML
    private TextField clientPhone;
    @FXML
    private TextField clientAddress;
    @FXML
    private TextField clientEmail;
    @FXML
    private TextField clientPassword;

    /**
     * Initializes the viewFactory instance, the bbdd instance, removes the context menus and hides the admin vbox.
     */
    public void initialize() {
        viewFactory = viewFactory.getInstance(null);
        adminName.setContextMenu(new ContextMenu());
        adminSurname.setContextMenu(new ContextMenu());
        adminNIF.setContextMenu(new ContextMenu());
        adminSSN.setContextMenu(new ContextMenu());
        adminSalary.setContextMenu(new ContextMenu());
        adminPassword.setContextMenu(new ContextMenu());
        clientName.setContextMenu(new ContextMenu());
        clientSurname.setContextMenu(new ContextMenu());
        clientNIF.setContextMenu(new ContextMenu());
        clientPhone.setContextMenu(new ContextMenu());
        clientAddress.setContextMenu(new ContextMenu());
        clientEmail.setContextMenu(new ContextMenu());
        clientPassword.setContextMenu(new ContextMenu());
        try {
        getInstance();
        } catch (HappyBankException e) {
            e.printStackTrace();
        }
        type.setValue("CLIENTE");
        if (type.getValue().equals("ADMINISTRADOR")) {
            Admin.setVisible(true);
            Client.setVisible(false);
        } else {
            Admin.setVisible(false);
            Client.setVisible(true);
        }
    }

    /**
     * Method that swaps the vbox depending on the selected type.
     */
    public void swapVbox() {
        if (type.getValue().equals("ADMINISTRADOR")) {
            Admin.setVisible(true);
            Client.setVisible(false);
            clientName.clear();
            clientAddress.clear();
            clientEmail.clear();
            clientNIF.clear();
            clientPhone.clear();
            clientSurname.clear();
            clientPassword.clear();
        } else {
            Admin.setVisible(false);
            Client.setVisible(true);
            adminName.clear();
            adminSurname.clear();
            adminNIF.clear();
            adminSSN.clear();
            adminSalary.clear();
            adminPassword.clear();
        }
    }

    /**
     * Method that adds a person to the database.
     */
    public void addPerson() {
        if (type.getValue().equals("ADMINISTRADOR")) {
            if (adminName.getText().isEmpty() || adminSurname.getText().isEmpty() || adminNIF.getText().isEmpty() || adminSSN.getText().isEmpty() || adminSalary.getText().isEmpty() || adminPassword.getText().isEmpty()) {
                viewFactory.showError(new FXMLLoader(getClass().getResource("/fxml/Error/emptyAddPersonFields.fxml")));
            } else {
                    Administrator adminToAdd = new Administrator(adminName.getText(), adminSurname.getText(), adminNIF.getText(), adminSSN.getText(), new BigDecimal(adminSalary.getText()), "HappyBank");
                try {
                    insertAdministrator(adminToAdd, adminPassword.getText());
                    adminName.clear();
                    adminSurname.clear();
                    adminNIF.clear();
                    adminSSN.clear();
                    adminSalary.clear();
                    adminPassword.clear();

                } catch (HappyBankException e) {
                        viewFactory.showError(new FXMLLoader(getClass().getResource("/fxml/Error/personAlreadyExists.fxml")));
                }
            }
        } else {
            if (clientName.getText().isEmpty() || clientSurname.getText().isEmpty() || clientNIF.getText().isEmpty() || clientPhone.getText().isEmpty() || clientAddress.getText().isEmpty() || clientEmail.getText().isEmpty() || clientPassword.getText().isEmpty()) {
                viewFactory.showError(new FXMLLoader(getClass().getResource("/fxml/Error/emptyAddPersonFields.fxml")));
            } else {
                    Client clientToAdd = new Client(clientName.getText(), clientSurname.getText(), clientNIF.getText(), clientEmail.getText(), clientPhone.getText(), clientAddress.getText(), "HappyBank");
                try {
                        insertClient(clientToAdd, clientPassword.getText());
                        clientName.clear();
                        clientSurname.clear();
                        clientNIF.clear();
                        clientPhone.clear();
                        clientAddress.clear();
                        clientEmail.clear();
                        clientPassword.clear();
                } catch (HappyBankException e) {
                        viewFactory.showError(new FXMLLoader(getClass().getResource("/fxml/Error/personAlreadyExists.fxml")));
                }
            }
        }
    }

    /**
     * Method that goes back to the client list view.
     */
    public void goBack() {
        if (type.getValue().equals("ADMINISTRADOR")) {
            if (adminName.getText().isEmpty() && adminSurname.getText().isEmpty() && adminNIF.getText().isEmpty() && adminSSN.getText().isEmpty() && adminSalary.getText().isEmpty() && adminPassword.getText().isEmpty()) {
                viewFactory.showClientList(administrator);
            } else {
                viewFactory.showError(new FXMLLoader(getClass().getResource("/fxml/Error/notEmptyAddPersonFields.fxml")));
            }
        } else {
            if (clientName.getText().isEmpty() && clientSurname.getText().isEmpty() && clientNIF.getText().isEmpty() && clientPhone.getText().isEmpty() && clientAddress.getText().isEmpty() && clientEmail.getText().isEmpty() && clientPassword.getText().isEmpty()) {
                viewFactory.showClientList(administrator);
            } else {
                viewFactory.showError(new FXMLLoader(getClass().getResource("/fxml/Error/notEmptyAddPersonFields.fxml")));
            }
        }
    }

    /**
     * Method that closes the session.
     */
    public void closeSession() {
        if (type.getValue().equals("ADMINISTRADOR")) {
            if (adminName.getText().isEmpty() && adminSurname.getText().isEmpty() && adminNIF.getText().isEmpty() && adminSSN.getText().isEmpty() && adminSalary.getText().isEmpty() && adminPassword.getText().isEmpty()) {
                viewFactory.showLoginView();
            } else {
                viewFactory.showError(new FXMLLoader(getClass().getResource("/fxml/Error/notEmptyAddPersonFields.fxml")));
            }
        } else {
            if (clientName.getText().isEmpty() && clientSurname.getText().isEmpty() && clientNIF.getText().isEmpty() && clientPhone.getText().isEmpty() && clientAddress.getText().isEmpty() && clientEmail.getText().isEmpty() && clientPassword.getText().isEmpty()) {
                viewFactory.showLoginView();
            } else {
                viewFactory.showError(new FXMLLoader(getClass().getResource("/fxml/Error/notEmptyAddPersonFields.fxml")));
            }
        }
    }

    /**
     * Method that goes to the main window.
     */
    public void goMain() {
        if (type.getValue().equals("ADMINISTRADOR")) {
            if (adminName.getText().isEmpty() && adminSurname.getText().isEmpty() && adminNIF.getText().isEmpty() && adminSSN.getText().isEmpty() && adminSalary.getText().isEmpty() && adminPassword.getText().isEmpty()) {
                viewFactory.showAdminMainWindow(administrator.getNIF());
            } else {
                viewFactory.showError(new FXMLLoader(getClass().getResource("/fxml/Error/notEmptyAddPersonFields.fxml")));
            }
        } else {
            if (clientName.getText().isEmpty() && clientSurname.getText().isEmpty() && clientNIF.getText().isEmpty() && clientPhone.getText().isEmpty() && clientAddress.getText().isEmpty() && clientEmail.getText().isEmpty() && clientPassword.getText().isEmpty()) {
                viewFactory.showAdminMainWindow(administrator.getNIF());
            } else {
                viewFactory.showError(new FXMLLoader(getClass().getResource("/fxml/Error/notEmptyAddPersonFields.fxml")));
            }
        }
    }

    /**
     * Method that sets the administrator.
     * @param administrator
     */
    public void setAdmin(Administrator administrator) {
        this.administrator = administrator;
        username = administrator.getName() + " " + administrator.getSurname();
        welcomeLabel.setText("¡Hola " + username + "!\nBienvenido al panel de \nadministración de HappyBank.");

    }
}
