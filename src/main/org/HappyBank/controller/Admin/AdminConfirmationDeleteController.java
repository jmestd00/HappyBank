package org.HappyBank.controller.Admin;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.util.Duration;
import org.HappyBank.model.Administrator;
import org.HappyBank.model.Client;
import org.HappyBank.model.*;
import org.HappyBank.view.ViewFactory;
import javafx.scene.control.Button;


/**
 * Controlador de la ventana de confirmación de eliminación de un cliente.
 */
public class AdminConfirmationDeleteController {
    private ViewFactory viewFactory;
    private BankService bankService = new BankService();
    private Client client;
    private Administrator administrator;
    @FXML
    private Button confirmDelete;
    @FXML
    private Button declineDelete;

    /**
     * Inicializa la ventana de confirmación de eliminación de un cliente. Hace que el botón de confirmación se habilite a los 3 segundos.
     */
    public void initialize() {
        viewFactory = viewFactory.getInstance(null);
        confirmDelete.setDisable(true);
        confirmDelete.setText("ELIMINAR");
        declineDelete.setText("CANCELAR");

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3),
                event -> confirmDelete.setDisable(false)
        ));
        timeline.setCycleCount(1);
        timeline.play();
    }

    /**
     * Método que establece los datos del cliente y del administrador.
     * @param client (cliente a eliminar)
     * @param admin (administrador que realiza la acción)
     */
    public void setData(Client client, Administrator admin) {
        this.client = client;
        this.administrator = admin;
    }

    /**
     * Método que elimina al cliente y cierra la ventana de confirmación.
     */
    public void delete() {
        bankService.deleteClient(client);
        viewFactory.closePopup();
        viewFactory.showClientList(administrator);
    }

    /**
     * Método que cierra la ventana de confirmación y vuelve a la ventana de edición de cliente.
     */
    public void decline() {
        viewFactory.closePopup();
        viewFactory.showAdminClientEditView(client, administrator);
    }
}
