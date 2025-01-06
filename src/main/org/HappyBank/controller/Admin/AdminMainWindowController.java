package org.HappyBank.controller.Admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.HappyBank.model.*;
import org.HappyBank.view.ViewFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import static org.HappyBank.model.DatabaseManager.*;


public class AdminMainWindowController {

    private String username;
    private String NIF;
    private Administrator admin;
    @FXML
    private Label welcomeLabel;
    private ViewFactory viewFactory = ViewFactory.getInstance(null);
    private boolean backUp = false;



    public void initialize() {
        // TODO
        try {
        getInstance();
        } catch (HappyBankException e) {
            e.printStackTrace();
        }
        readConfig();
        if (backUp) {
            //doBackUp();
        }

    }

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

    public void setNIF(String NIF) {
        this.NIF = NIF;
        try {
        admin = getAdministrator(NIF);
        } catch (HappyBankException e) {
            e.printStackTrace();
        }
        username = admin.getName() + " " + admin.getSurname();
        welcomeLabel.setText("¡Hola " + username + "!\nBienvenido al panel de \nadministración de HappyBank.");
        initialize();
    }

    public void closeSession() {
        viewFactory.showLoginView();
    }

    public void showClientList() {
        viewFactory.showClientList(admin);
    }

    public void showLegend() {
        viewFactory.showAdminLegend();
    }
}
