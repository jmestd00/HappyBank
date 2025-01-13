package org.HappyBank.controller.Admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.HappyBank.model.Administrator;
import org.HappyBank.model.*;
import org.HappyBank.view.ViewFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Controlador para la vista de modificación de cliente.
 */
public class AdminModifyClientController {
private static final Logger logger = LogManager.getLogger(AdminModifyClientController.class.getName());
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
     * Inicializa la vista de modificación de cliente.
     */
    public void initialize() {
        viewFactory = viewFactory.getInstance(null);
    }

    /**
     * Método que establece los datos del cliente y del administrador en la vista.
     * @param client (El cliente a modificar)
     * @param administrator (El administrador que realiza la acción)
     */
    public void setData(Client client, Administrator administrator) {
        this.client = client;
        this.admin = administrator;
        username = this.admin.getName() + " " + this.admin.getSurname();
        welcomeLabel.setText("¡Hola " + username + "!\nBienvenido al panel de \nadministración de HappyBank.");
        clientLabel.setText("Cliente: " + client.getName() + " " + client.getSurname());
    }

    /**
     * Método que abre la ventana de confirmación para eliminar un cliente.
     */
    public void deleteUser() {
        viewFactory.showConfirmationWindow(client, admin);
    }

    /**
     * Método que modifica los datos del cliente.
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
     * Método que vuelve a la ventana de lista de clientes.
     */
    public void goBack() {
        if (checkCritic()) {
            viewFactory.showError(new FXMLLoader(getClass().getResource("/fxml/Error/NotEmptyModifyPersonFields.fxml")));
            logger.error("Los campos para modificar no están vacíos por lo que no se puede volver a la ventana anterior.");
        } else {
            viewFactory.showClientList(admin);
        }
    }

    /**
     * Método que abre la ventana para confirmar el cierre de sesión.
     */
    public void closeSession() {
        if (checkCritic()) {
            viewFactory.showError(new FXMLLoader(getClass().getResource("/fxml/Error/NotEmptyModifyPersonFields.fxml")));
            logger.error("Los campos para modificar no están vacíos por lo que no se puede cerrar sesión.");
        } else {
            viewFactory.showCloseSessionConfirmation();
        }
    }

    /**
     * Método que muestra la leyenda de la vista.
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
