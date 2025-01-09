package org.HappyBank.controller.Client;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.HappyBank.model.Client;
import org.HappyBank.view.ViewFactory;

/**
 * Controlador para la vista de datos personales de cliente.
 */
public class ClientPersonalDataController {
    private Client client;
    private String username;
    @FXML
    private Label welcomeLabel;
    @FXML
    private TextField fullName;
    @FXML
    private TextField nif;
    @FXML
    private TextField phone;
    @FXML
    private TextField email;
    @FXML
    private TextField address;
    private ViewFactory viewFactory;

    /**
     * Método que inicializa la vista de datos personales de cliente.
     */
    public void initialize() {
    viewFactory = ViewFactory.getInstance(null);
    }

    /**
     * Método que establece los datos del cliente en la vista.
     * @param client (El cliente que realiza la consulta)
     */
    public void setData(Client client) {
        this.client = client;
        this.username = client.getName() + " " + client.getSurname();
        welcomeLabel.setText("¡Hola " + username + "!\nBienvenido al panel de \nclientes de HappyBank.");
        fullName.setText(client.getName() + " " + client.getSurname());
        nif.setText(client.getNIF());
        phone.setText(client.getPhone());
        email.setText(client.getEmail());
        address.setText(client.getAddress());
    }

    /**
     * Método que cierra la sesión del cliente.
     */
    public void closeSession() {
        viewFactory.showCloseSessionConfirmation();
    }

    /**
     * Método que muestra la ventana de inicio.
     */
    public void goBack() {
        viewFactory.showClientMainWindow(client.getNIF());
    }

    /**
     * Método que muestra la leyenda de la vista.
     */
    public void showLegend() {
        viewFactory.closeLegend();
        viewFactory.showClientLegend();
    }
}
