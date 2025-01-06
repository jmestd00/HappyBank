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

public class ViewFactory {

    private static ViewFactory vFactoryInstance;

    Image happyBankLogo = new Image(String.valueOf(getClass().getResource("/images/HappyBank512_512.png")));

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

    private ViewFactory(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public static ViewFactory getInstance(Stage primaryStage) {
        if (vFactoryInstance == null) {
            vFactoryInstance = new ViewFactory(primaryStage);
        } else if (primaryStage != null) {
            vFactoryInstance.primaryStage = primaryStage;
        }
        return vFactoryInstance;
    }

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

    public void showError(FXMLLoader fxmlLoader) {
        // Show error
        try {
            // Cargar el archivo FXML del popup
            Parent popupRoot = fxmlLoader.load();
            popupStage = new Stage();
            popupStage.resizableProperty().setValue(Boolean.FALSE);
            popupStage.setTitle("ERROR");
            //popupStage.initModality(Modality.APPLICATION_MODAL); // Bloquear la ventana principal
            popupStage.setScene(new Scene(popupRoot));
            popupStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/warn_icon.png")));
            popupStage.centerOnScreen();
            popupStage.show();
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), // Duración antes de ejecutar la acción
                    event -> this.closePopup() // Acción para cerrar la ventana
            ));
            timeline.setCycleCount(1); // Ejecutar solo una vez
            timeline.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    public void closePopup() {
        popupStage.close();
    }
}
