package org.HappyBank.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.HappyBank.controller.Admin.*;
import org.HappyBank.controller.Client.*;
import org.HappyBank.controller.*;
import org.HappyBank.model.Administrator;
import org.HappyBank.model.Client;

import java.awt.*;
import java.util.Objects;

/**
 * This class is responsible for managing the views of the application.
 */
public class ViewFactory {

    private static ViewFactory vFactoryInstance;

    Image happyBankLogo = new Image(String.valueOf(getClass().getResource("/images/bankLogo.png")));

    // The two stages of the application (primary for all the windows of the app and popup for the errors like wrong login)
    private Stage primaryStage;
    private Stage popupStage = new Stage();

    // All the windows' controllers for all the main windows, to be able to pass the arguments needeed between controllers
    /* Admin */
    private AdminMainWindowController adminMainWindow;
    private AdminModifyClientController modifyClient;
    private AdminClientListController clientList;
    private AdminClientTransactionHistoryController transactionHistory;
    private AdminAddPersonController addPerson;
    private AdminLegendController legend;
    private AdminConfirmationDeleteController confirmationDelete;
    /* Client */
    private ClientMainWindowController clientMainWindow;
    private ClientPerformTransactionController performTransaction;
    private ClientPersonalDataController personalData;
    private ClientTransactionListController transactionList;
    private ClientViewAccountController viewAccount;
    /* Error */
    private ErrorController errorController;
    /* Login */
    private LoginController loginController;

    /**
     * Constructor of the class ViewFactory to use the singleton pattern
     * @param primaryStage
     */
    private ViewFactory(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Method to get the instance of the class ViewFactory
     * @param primaryStage
     * @return vFactoryInstance (the instance of the class ViewFactory)
     */
    public static ViewFactory getInstance(Stage primaryStage) {
        if (vFactoryInstance == null) {
            vFactoryInstance = new ViewFactory(primaryStage);
        } else if (primaryStage != null) {
            vFactoryInstance.primaryStage = primaryStage;
        }
        return vFactoryInstance;
    }

    /**
     * Method to show the login window
     */
    public void showLoginView() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/loginView.fxml")));
            Scene scene = new Scene(root);
            primaryStage.setTitle("HappyBank");
            primaryStage.setScene(scene);
            primaryStage.getIcons().add(happyBankLogo);
            primaryStage.resizableProperty().setValue(Boolean.FALSE);
            primaryStage.centerOnScreen();
            primaryStage.show();
            primaryStage.setOnCloseRequest(event -> System.exit(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

/* Admin */
    /**
     * Method to show the window to view the main window of the administrator
     * @param username
     */
    public void showAdminMainWindow(String username) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Admin/AdminMainWindow.fxml"));
            Parent root = loader.load();
            adminMainWindow = loader.getController();
            primaryStage.setTitle("HappyBank");
            primaryStage.setScene(new Scene(root));
            primaryStage.getIcons().add(happyBankLogo);
            primaryStage.resizableProperty().setValue(Boolean.FALSE);
            primaryStage.centerOnScreen();
            adminMainWindow.setNIF(username);
            primaryStage.show();
            primaryStage.setOnCloseRequest(event -> System.exit(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to show the window to confirm the delete operation of a client
     * @param client
     * @param admin
     */
    public void showConfirmationWindow(Client client, Administrator admin) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/Admin/AdminConfirmationDelete.fxml")));
            Parent root = loader.load();
            confirmationDelete = loader.getController();
            confirmationDelete.setData(client, admin);
            popupStage.setTitle("HappyBank");
            popupStage.setScene(new Scene(root));
            popupStage.getIcons().add(happyBankLogo);
            popupStage.resizableProperty().setValue(Boolean.FALSE);
            popupStage.centerOnScreen();
            popupStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to show the window to view the bank's client list
     * @param admin
     */
    public void showClientList(Administrator admin) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/Admin/AdminClientList.fxml")));
            Parent root = loader.load();
            clientList = loader.getController();
            primaryStage.setTitle("HappyBank");
            primaryStage.setScene(new Scene(root));
            primaryStage.getIcons().add(happyBankLogo);
            primaryStage.resizableProperty().setValue(Boolean.FALSE);
            clientList.setAdmin(admin);
            primaryStage.centerOnScreen();
            primaryStage.show();
            primaryStage.setOnCloseRequest(event -> System.exit(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to show the legend window to provide information about the buttons of the application
     */
    public void showAdminLegend() {
        try {
            // Cargar el archivo FXML del popup
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/Admin/AdminLegend.fxml")));
            Parent popupRoot = loader.load();
            popupStage = new Stage();
            popupStage.resizableProperty().setValue(Boolean.FALSE);
            popupStage.setTitle("Leyenda");
            popupStage.initModality(Modality.APPLICATION_MODAL); // Bloquear la ventana principal
            popupStage.setScene(new Scene(popupRoot));
            popupStage.centerOnScreen();
            popupStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to show the window to edit the personal data of a client
     * @param selectedClient
     * @param administrator
     */
    public void showAdminClientEditView(Client selectedClient, Administrator administrator) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/Admin/AdminModifyClient.fxml")));
            Parent root = loader.load();
            modifyClient = loader.getController();
            primaryStage.setTitle("HappyBank");
            primaryStage.setScene(new Scene(root));
            primaryStage.getIcons().add(happyBankLogo);
            primaryStage.resizableProperty().setValue(Boolean.FALSE);
            modifyClient.setData(selectedClient, administrator);
            primaryStage.centerOnScreen();
            primaryStage.show();
            primaryStage.setOnCloseRequest(event -> System.exit(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to show the window to view the transaction history of a client
     * @param selectedClient
     * @param administrator
     */
    public void showAdminTransactionHistoryView(Client selectedClient, Administrator administrator) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/Admin/AdminClientTransactionHistory.fxml")));
            Parent root = loader.load();
            transactionHistory = loader.getController();
            primaryStage.setTitle("HappyBank");
            primaryStage.setScene(new Scene(root));
            primaryStage.getIcons().add(happyBankLogo);
            primaryStage.resizableProperty().setValue(Boolean.FALSE);
            transactionHistory.setData(selectedClient, administrator);
            primaryStage.centerOnScreen();
            primaryStage.show();
            primaryStage.setOnCloseRequest(event -> System.exit(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to show the window to add a new client
     * @param admin
     */
    public void showAddClient(Administrator admin) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/Admin/AdminAddPerson.fxml")));
            Parent root = loader.load();
            addPerson = loader.getController();
            primaryStage.setTitle("HappyBank");
            primaryStage.setScene(new Scene(root));
            primaryStage.getIcons().add(happyBankLogo);
            primaryStage.resizableProperty().setValue(Boolean.FALSE);
            addPerson.setAdmin(admin);
            primaryStage.centerOnScreen();
            primaryStage.show();
            primaryStage.setOnCloseRequest(event -> System.exit(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
/* Client */
    /**
     * Method to show the main window of the client
     * @param username
     */
    public void showClientMainWindow(String username) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/Client/ClientMainWindow.fxml")));
            primaryStage.setTitle("HappyBank");
            primaryStage.setScene(new Scene(root));
            primaryStage.getIcons().add(happyBankLogo);
            primaryStage.resizableProperty().setValue(Boolean.FALSE);
            primaryStage.centerOnScreen();
            primaryStage.show();
            primaryStage.setOnCloseRequest(event -> System.exit(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

/* Auxiliar Methods */
    /**
     * Method to close the popups windows that haven't a established time to close
     */
    public void closePopup() {
        popupStage.close();
    }

    /**
     * Method to show the window error
     * @param fxmlLoader
     */
    public void showError(FXMLLoader fxmlLoader) {
        try {
            Parent popupRoot = fxmlLoader.load();
            popupStage = new Stage();
            popupStage.resizableProperty().setValue(Boolean.FALSE);
            popupStage.setTitle("ERROR");
            popupStage.setScene(new Scene(popupRoot));
            popupStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/warn_icon.png")));
            popupStage.centerOnScreen();
            popupStage.show();
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3),
                    event -> this.closePopup()
            ));
            timeline.setCycleCount(1);
            timeline.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
