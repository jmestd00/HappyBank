package org.HappyBank.controller.Admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.HappyBank.model.*;
import org.HappyBank.view.ViewFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;


/**
 * Controlador de la ventana principal del administrador.
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
     * Método que inicializa la ventana principal del administrador. Lee la configuración para ver si la opción de backup está activada.
     */
    public void initialize() {

    }

    /**
     * Método que establece el NIF del administrador.
     * @param NIF (NIF del administrador)
     */
    public void setNIF(String NIF) {
        this.NIF = NIF;
        admin = bankService.getAdministrator(NIF);
        username = admin.getName() + " " + admin.getSurname();
        welcomeLabel.setText("¡Hola " + username + "!\nBienvenido al panel de \nadministración de HappyBank.");
        initialize();
    }

    /**
     * Método que cierra la sesión del administrador.
     */
    public void closeSession() {
        viewFactory.showCloseSessionConfirmation();
    }

    /**
     * Método que muestra la ventana de la lista de clientes.
     */
    public void showClientList() {
        viewFactory.showClientList(admin);
    }

    /**
     * Método que muestra la ventana de la leyenda de la parte administrativa.
     */
    public void showLegend() {
        viewFactory.closeLegend();
        viewFactory.showAdminLegend();
    }
}
