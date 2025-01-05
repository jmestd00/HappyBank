package org.HappyBank.controller.Admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.HappyBank.model.Administrator;
import org.HappyBank.model.Client;

import org.HappyBank.view.ViewFactory;
public class AdminModifyClientController {
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
/*
    @FXML
    @FXML
    @FXML
    @FXML
*/

    public void initialize() {
        viewFactory = viewFactory.getInstance(null);
    }

    public void setData(Client client, Administrator administrator) {
        this.client = client;
        this.admin = administrator;
        username = this.admin.getName() + " " + this.admin.getSurname();
        welcomeLabel.setText("¡Hola " + username + "!\nBienvenido al panel de \nadministración de HappyBank.");
        clientLabel.setText("Cliente: " + client.getName() + " " + client.getSurname());
    }

    public void deleteUser() {
        viewFactory.showConfirmationWindow(client, admin);
    }

    public void modifyClient() {
        if (!phone.getText().isEmpty()) {
            client.setPhone(phone.getText());
        }
        if (!email.getText().isEmpty()) {
            client.setEmail(email.getText());
        }
        if (!address.getText().isEmpty()) {
            client.setAddress(address.getText());
        }
        if (!password.getText().isEmpty()) {
            //lta implementar el cambio de contraseña
        }
    }

    public void goBack() {
        if (checkCritic()) {
            viewFactory.showError(new FXMLLoader(getClass().getResource("/fxml/Error/notEmptyModifyPersonFields.fxml")));
        } else {
            viewFactory.showClientList(admin);
        }
    }
        public void closeSession () {
            if (checkCritic()) {
                viewFactory.showError(new FXMLLoader(getClass().getResource("/fxml/Error/notEmptyModifyPersonFields.fxml")));
            } else {
                viewFactory.showLoginView();
            }
        }

        public void goMain () {
            if (checkCritic()) {
                viewFactory.showError(new FXMLLoader(getClass().getResource("/fxml/Error/notEmptyModifyPersonFields.fxml")));
            } else {
                viewFactory.showAdminMainWindow(admin.getNIF());
            }
        }

        private boolean checkCritic () {
            if (!phone.getText().isEmpty() || !email.getText().isEmpty() || !address.getText().isEmpty() || !password.getText().isEmpty()) {
                return true;
            } else {
                return false;
            }
        }



}
