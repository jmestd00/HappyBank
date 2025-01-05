package org.HappyBank.controller.Admin;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.util.Duration;
import org.HappyBank.model.Administrator;
import org.HappyBank.model.Client;
import org.HappyBank.model.HappyBankException;
import org.HappyBank.view.ViewFactory;
import javafx.scene.control.Button;

import static org.HappyBank.model.DatabaseManager.*;

public class AdminConfirmationDeleteController {
    private ViewFactory viewFactory;
    private Client client;
    private Administrator administrator;
    @FXML
    private Button confirmDelete;
    @FXML
    private Button declineDelete;

    public void initialize() {
        viewFactory = viewFactory.getInstance(null);
        confirmDelete.setDisable(true);
        confirmDelete.setText("ELIMINAR");
        declineDelete.setText("CANCELAR");
        try {
            getInstance();
        }catch (HappyBankException e) {
            e.printStackTrace();
        }
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3),
                event -> confirmDelete.setDisable(false)
        ));
        timeline.setCycleCount(1);
        timeline.play();
    }

    public void setData(Client client, Administrator admin) {
        this.client = client;
        this.administrator = admin;
    }

    public void delete() {
        try {
        deleteClient(client.getNIF());
        } catch (HappyBankException e) {
            e.printStackTrace();
        }
        viewFactory.closePopup();
        viewFactory.showClientList(administrator);
    }

    public void decline() {
        viewFactory.closePopup();
        viewFactory.showAdminClientEditView(client, administrator);
    }
}
