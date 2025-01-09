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
 * Controller for the confirmation window to delete a user.
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
     * Initializes the viewFactory instance, a countdown to enable the delete button and the confirmation window.
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
     * Sets the data of the client and the administrator.
     * @param client
     * @param admin
     */
    public void setData(Client client, Administrator admin) {
        this.client = client;
        this.administrator = admin;
    }

    /**
     * Method that deletes the user from the database and closes the confirmation window.
     */
    public void delete() {
        bankService.deleteClient(client);
        viewFactory.closePopup();
        viewFactory.showClientList(administrator);
    }

    /**
     * Method that closes the confirmation window and opens the edit client view.
     */
    public void decline() {
        viewFactory.closePopup();
        viewFactory.showAdminClientEditView(client, administrator);
    }
}
