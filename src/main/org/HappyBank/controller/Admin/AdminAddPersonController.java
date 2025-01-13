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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;




/**
 * Controlador de la ventana de añadir una persona.
 */
public class AdminAddPersonController {
    private static final Logger logger = LogManager.getLogger(AdminAddPersonController.class.getName());
    private Administrator administrator;
    private BankService bankService = new BankService();
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
     * Inicializa la ventana de añadir una persona.
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
     * Método que intercambia el VBox entre los campos de admin y de cliente.
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
            logger.info("Se ha seleccionado el tipo de persona: Administrador");
        } else {
            Admin.setVisible(false);
            Client.setVisible(true);
            adminName.clear();
            adminSurname.clear();
            adminNIF.clear();
            adminSSN.clear();
            adminSalary.clear();
            adminPassword.clear();
            logger.info("Se ha seleccionado el tipo de persona: Cliente");
        }
    }

    /**
     * Método que añade una persona a la base de datos.
     */
    public void addPerson() {
        if (type.getValue().equals("ADMINISTRADOR")) {
            if (adminName.getText().isEmpty() || adminSurname.getText().isEmpty() || adminNIF.getText().isEmpty() || adminSSN.getText().isEmpty() || adminSalary.getText().isEmpty() || adminPassword.getText().isEmpty()) {
                viewFactory.showError(new FXMLLoader(getClass().getResource("/fxml/Error/EmptyAddPersonFields.fxml")));
                logger.error("No se han rellenado todos los campos para añadir un administrador.");
            } else {
                try {
                    logger.info("El administrador " + administrator.getNIF() + " está añadiendo un administrador a la base de datos.");
                    bankService.createAdministrator(adminName.getText(), adminSurname.getText(), adminNIF.getText(), adminSSN.getText(), new BigDecimal(adminSalary.getText()), "HappyBank", adminPassword.getText());
                } catch (RuntimeException e) {
                    viewFactory.showError(new FXMLLoader(getClass().getResource("/fxml/Error/PersonAlreadyExists.fxml")));
                    logger.error("El administrador ya existe en la base de datos.");
                }
                adminName.clear();
                adminSurname.clear();
                adminNIF.clear();
                adminSSN.clear();
                adminSalary.clear();
                adminPassword.clear();
            }
        } else {
            if (clientName.getText().isEmpty() || clientSurname.getText().isEmpty() || clientNIF.getText().isEmpty() || clientPhone.getText().isEmpty() || clientAddress.getText().isEmpty() || clientEmail.getText().isEmpty() || clientPassword.getText().isEmpty()) {
                viewFactory.showError(new FXMLLoader(getClass().getResource("/fxml/Error/EmptyAddPersonFields.fxml")));
                logger.error("No se han rellenado todos los campos para añadir un cliente.");
            } else {
                try {
                logger.info("El administrador " + administrator.getNIF() + " está añadiendo un cliente a la base de datos.");
                bankService.createClient(clientName.getText(),clientSurname.getText(),clientNIF.getText(),
                        clientEmail.getText(), clientPhone.getText(), clientAddress.getText(), "HappyBank", clientPassword.getText());
                } catch (RuntimeException e) {
                viewFactory.showError(new FXMLLoader(getClass().getResource("/fxml/Error/PersonAlreadyExists.fxml")));
                logger.error("El cliente ya existe en la base de datos.");
                }
                clientName.clear();
                clientSurname.clear();
                clientNIF.clear();
                clientPhone.clear();
                clientAddress.clear();
                clientEmail.clear();
                clientPassword.clear();
            }
        }
    }

    /**
     * Método que cierra la ventana de añadir una persona.
     */
    public void goBack() {
        if (type.getValue().equals("ADMINISTRADOR")) {
            if (adminName.getText().isEmpty() && adminSurname.getText().isEmpty() && adminNIF.getText().isEmpty() && adminSSN.getText().isEmpty() && adminSalary.getText().isEmpty() && adminPassword.getText().isEmpty()) {
                viewFactory.showClientList(administrator);
                logger.info("Se ha cerrado la ventana de añadir una persona volviendo a la lista de clientes.");
            } else {
                viewFactory.showError(new FXMLLoader(getClass().getResource("/fxml/Error/NotEmptyAddPersonFields.fxml")));
                logger.error("No se ha cerrado la ventana de añadir una persona porque hay campos no vacíos.");
            }
        } else {
            if (clientName.getText().isEmpty() && clientSurname.getText().isEmpty() && clientNIF.getText().isEmpty() && clientPhone.getText().isEmpty() && clientAddress.getText().isEmpty() && clientEmail.getText().isEmpty() && clientPassword.getText().isEmpty()) {
                viewFactory.showClientList(administrator);
                logger.info("Se ha cerrado la ventana de añadir una persona volviendo a la lista de clientes.");
            } else {
                viewFactory.showError(new FXMLLoader(getClass().getResource("/fxml/Error/NotEmptyAddPersonFields.fxml")));
                logger.error("No se ha cerrado la ventana de añadir una persona porque hay campos no vacíos.");
            }
        }
    }

    /**
     * Método que muestra el mensaje de confirmación de cierre de sesión.
     */
    public void closeSession() {
        if (type.getValue().equals("ADMINISTRADOR")) {
            if (adminName.getText().isEmpty() && adminSurname.getText().isEmpty() && adminNIF.getText().isEmpty() && adminSSN.getText().isEmpty() && adminSalary.getText().isEmpty() && adminPassword.getText().isEmpty()) {
                viewFactory.showCloseSessionConfirmation();
                logger.info("Se ha mostrado la ventana de confirmación de cierre de sesión desde la ventana de añadir una persona.");
            } else {
                viewFactory.showError(new FXMLLoader(getClass().getResource("/fxml/Error/NotEmptyAddPersonFields.fxml")));
                logger.error("No se ha mostrado la ventana de confirmación de cierre de sesión porque hay campos no vacíos.");
            }
        } else {
            if (clientName.getText().isEmpty() && clientSurname.getText().isEmpty() && clientNIF.getText().isEmpty() && clientPhone.getText().isEmpty() && clientAddress.getText().isEmpty() && clientEmail.getText().isEmpty() && clientPassword.getText().isEmpty()) {
                viewFactory.showCloseSessionConfirmation();
                logger.info("Se ha mostrado la ventana de confirmación de cierre de sesión desde la ventana de añadir una persona.");
            } else {
                viewFactory.showError(new FXMLLoader(getClass().getResource("/fxml/Error/NotEmptyAddPersonFields.fxml")));
                logger.error("No se ha mostrado la ventana de confirmación de cierre de sesión porque hay campos no vacíos.");
            }
        }
    }

    /**
     * Método que muestra la leyenda.
     */
    public void showLegend() {
        viewFactory.closeLegend();
        viewFactory.showAdminLegend();
    }

    /**
     * Método que establece el administrador.
     * @param administrator (administrador que realiza la acción)
     */
    public void setAdmin(Administrator administrator) {
        this.administrator = administrator;
        username = administrator.getName() + " " + administrator.getSurname();
        welcomeLabel.setText("¡Hola " + username + "!\nBienvenido al panel de \nadministración de HappyBank.");

    }
}
