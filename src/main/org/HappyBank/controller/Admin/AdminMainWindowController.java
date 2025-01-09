package org.HappyBank.controller.Admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.HappyBank.model.*;
import org.HappyBank.view.ViewFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;


/**
 * Controller for the main window of the administrator.
 */
public class AdminMainWindowController {

    private String username;
    private String NIF;
    private Administrator admin;
    @FXML
    private Label welcomeLabel;
    private ViewFactory viewFactory = ViewFactory.getInstance(null);
    private boolean backUp = false;
    private BankService bankService = new BankService();


    /**
     * Initializes the viewFactory instance and reads the configuration file to perform the backup or not.
     */
    public void initialize() {

        readConfig();
        if (backUp) {
            //doBackUp();
        }

    }

    /**
     * Reads the configuration file to check if the backup option is enabled.
     */
    private void readConfig() {
        Properties prop = new Properties();
        try {
            File file = new File("options.config");
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                prop.load(fis);
                fis.close();
                backUp = Boolean.parseBoolean(prop.getProperty("bbddBackup"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method tha set the NIF of the administrator and initializes the view.
     * @param NIF
     */
    public void setNIF(String NIF) {
        this.NIF = NIF;
        admin = bankService.getAdministrator(NIF);
        username = admin.getName() + " " + admin.getSurname();
        welcomeLabel.setText("¡Hola " + username + "!\nBienvenido al panel de \nadministración de HappyBank.");
        initialize();
    }

    /**
     * Method that goes to the login view.
     */
    public void closeSession() {
        viewFactory.showCloseSessionConfirmation();
    }

    /**
     * Method that goes to the client list view.
     */
    public void showClientList() {
        viewFactory.showClientList(admin);
    }

    /**
     * Method that shows the legend of the administrator part of the application.
     */
    public void showLegend() {
        viewFactory.closeLegend();
        viewFactory.showAdminLegend();
    }
}
