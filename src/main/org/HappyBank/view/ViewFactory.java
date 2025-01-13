package org.HappyBank.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.HappyBank.controller.Admin.*;
import org.HappyBank.controller.Client.*;
import org.HappyBank.controller.*;
import org.HappyBank.model.Account;
import org.HappyBank.model.Administrator;
import org.HappyBank.model.Client;

import java.io.IOException;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Clase que se encarga de gestionar las vistas de la aplicación.
 */
public class ViewFactory {

private static final Logger logger = LogManager.getLogger(ViewFactory.class.getName());
    private static ViewFactory vFactoryInstance;

    Image happyBankLogo = new Image(String.valueOf(getClass().getResource("/images/bankLogo.png")));

    // The two stages of the application (primary for all the windows of the app, the legend popup, the concept popup
    // and popup for the errors like wrong login)
    private Stage primaryStage;
    private Stage popupStage = new Stage();
    private Stage legendStage = new Stage();
    private Stage conceptStage = new Stage();


    // All the windows' controllers for all the main windows, to be able to pass the arguments needeed between controllers
    /* Admin */
    private AdminMainWindowController adminMainWindow;
    private AdminModifyClientController modifyClient;
    private AdminClientListController clientList;
    private AdminClientTransactionHistoryController transactionHistory;
    private AdminAddPersonController addPerson;
    private AdminConfirmationDeleteController confirmationDelete;
    /* Client */
    private ClientMainWindowController clientMainWindow;
    private ClientPerformTransactionController performTransaction;
    private ClientPersonalDataController personalData;
    private ClientTransactionListController transactionList;
    /* Common */
    private TransactionConceptController transactionConcept;
    private DatabaseBackUpController backUp = new DatabaseBackUpController();


    /**
     * Constructor of the class ViewFactory usado por el patrón singleton.
     * @param primaryStage
     */
    private ViewFactory(Stage primaryStage) {
        this.primaryStage = primaryStage;
        popupStage.initModality(Modality.APPLICATION_MODAL);
    }

    /**
     * Método que devuelve la instancia de la clase ViewFactory.
     * @param primaryStage
     * @return vFactoryInstance (la instancia de la clase ViewFactory)
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
     * Este método se encarga de mostrar la ventana de inicio de sesión.
     */
    public void showLoginView() {
        try {
            logger.info("Se ha iniciado la aplicación.");
            logger.info("Se ha cargado la ventana de inicio de sesión.");
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/LoginView.fxml")));
            Scene scene = new Scene(root);
            primaryStage.setTitle("HappyBank - Inicio de Sesión");
            primaryStage.setScene(scene);
            primaryStage.getIcons().add(happyBankLogo);
            primaryStage.resizableProperty().setValue(Boolean.FALSE);
            primaryStage.centerOnScreen();
            primaryStage.show();
            primaryStage.setOnCloseRequest(event -> {
                System.exit(0);});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

/* Admin */
    /**
     * Este método se encarga de mostrar la ventana principal del administrador.
     * @param username (NIF del administrador)
     */
    public void showAdminMainWindow(String username) {
        try {
            logger.info("Se ha cargado la ventana principal del administrador " + username + ".");
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
            primaryStage.setOnCloseRequest(event -> {backUp.doBackUp();System.exit(0);});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Este método se encarga de mostrar la ventana de confirmación para eliminar un cliente.
     * @param client (Cliente a eliminar)
     * @param admin (Administrador que realiza la acción)
     */
    public void showConfirmationWindow(Client client, Administrator admin) {
        try {
            logger.info("El administrador " + admin.getNIF() + " ha solicitado la ventana de confirmación de eliminación del cliente " + client.getNIF() + ".");
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/Admin/AdminConfirmationDelete.fxml")));
            Parent root = loader.load();
            confirmationDelete = loader.getController();
            confirmationDelete.setData(client, admin);
            popupStage.setTitle("HappyBank - Confirmar Eliminación");
            popupStage.setScene(new Scene(root));
            popupStage.getIcons().add(happyBankLogo);
            popupStage.resizableProperty().setValue(Boolean.FALSE);
            popupStage.centerOnScreen();
            popupStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Este método se encarga de mostrar la ventana de la lista de clientes del banco.
     * @param admin (Administrador que realiza la acción)
     */
    public void showClientList(Administrator admin) {
        try {
            logger.info("El administrador " + admin.getNIF() + " ha solicitado la lista de clientes.");
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/Admin/AdminClientList.fxml")));
            Parent root = loader.load();
            clientList = loader.getController();
            primaryStage.setTitle("HappyBank - Lista de Clientes");
            primaryStage.setScene(new Scene(root));
            primaryStage.getIcons().add(happyBankLogo);
            primaryStage.resizableProperty().setValue(Boolean.FALSE);
            clientList.setAdmin(admin);
            primaryStage.centerOnScreen();
            primaryStage.show();
            primaryStage.setOnCloseRequest(event -> {backUp.doBackUp();System.exit(0);});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Este método se encarga de mostrar la ventana de la leyenda de la parte administrativa.
     */
    public void showAdminLegend() {
        try {
            // Cargar el archivo FXML del popup
            logger.info("Se ha solicitado la leyenda de la parte administrativa.");
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/Admin/AdminLegend.fxml")));
            Parent popupRoot = loader.load();
            legendStage = new Stage();
            legendStage.resizableProperty().setValue(Boolean.FALSE);
            legendStage.setTitle("Leyenda");
            legendStage.getIcons().add(happyBankLogo);
            legendStage.setScene(new Scene(popupRoot));
            legendStage.centerOnScreen();
            legendStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Este método se encarga de mostrar la ventana de edición de un cliente.
     * @param selectedClient (cliente a editar)
     * @param administrator (administrador que realiza la acción)
     */
    public void showAdminClientEditView(Client selectedClient, Administrator administrator) {
        try {
            logger.info("El administrador " + administrator.getNIF() + " ha solicitado la ventana de edición del cliente " + selectedClient.getNIF() + ".");
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/Admin/AdminModifyClient.fxml")));
            Parent root = loader.load();
            modifyClient = loader.getController();
            primaryStage.setTitle("HappyBank - Modificar Cliente");
            primaryStage.setScene(new Scene(root));
            primaryStage.getIcons().add(happyBankLogo);
            primaryStage.resizableProperty().setValue(Boolean.FALSE);
            modifyClient.setData(selectedClient, administrator);
            primaryStage.centerOnScreen();
            primaryStage.show();
            primaryStage.setOnCloseRequest(event -> {backUp.doBackUp();System.exit(0);});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Este método se encarga de mostrar el historial de transacciones de un cliente.
     * @param selectedClient (cliente seleccionado)
     * @param administrator (administrador que realiza la acción)
     */
    public void showAdminTransactionHistoryView(Client selectedClient, Administrator administrator) {
        try {
            logger.info("El administrador " + administrator.getNIF() + " ha solicitado el historial de transacciones del cliente " + selectedClient.getNIF() + ".");
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/Admin/AdminClientTransactionHistory.fxml")));
            Parent root = loader.load();
            transactionHistory = loader.getController();
            primaryStage.setTitle("HappyBank - Historial de Transacciones");
            primaryStage.setScene(new Scene(root));
            primaryStage.getIcons().add(happyBankLogo);
            primaryStage.resizableProperty().setValue(Boolean.FALSE);
            transactionHistory.setData(selectedClient, administrator);
            primaryStage.centerOnScreen();
            primaryStage.show();
            primaryStage.setOnCloseRequest(event -> {backUp.doBackUp();System.exit(0);});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Este método se encarga de mostrar la ventana de añadir un cliente.
     * @param admin (administrador que realiza la acción)
     */
    public void showAddClient(Administrator admin) {
        try {
            logger.info("El administrador " + admin.getNIF() + " ha solicitado la ventana de añadir un cliente.");
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/Admin/AdminAddPerson.fxml")));
            Parent root = loader.load();
            addPerson = loader.getController();
            primaryStage.setTitle("HappyBank - Añadir Cliente");
            primaryStage.setScene(new Scene(root));
            primaryStage.getIcons().add(happyBankLogo);
            primaryStage.resizableProperty().setValue(Boolean.FALSE);
            addPerson.setAdmin(admin);
            primaryStage.centerOnScreen();
            primaryStage.show();
            primaryStage.setOnCloseRequest(event -> {backUp.doBackUp();System.exit(0);});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
/* Client */
    /**
     * Method to show the main window of the client.
     * @param username
     */
    public void showClientMainWindow(String username) {
        try {
            logger.info("Se ha cargado la ventana principal del cliente " + username + ".");
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/Client/ClientMainWindow.fxml")));
            Parent root = loader.load();
            clientMainWindow = loader.getController();
            clientMainWindow.setNIF(username);
            primaryStage.setTitle("HappyBank");
            primaryStage.setScene(new Scene(root));
            primaryStage.getIcons().add(happyBankLogo);
            primaryStage.resizableProperty().setValue(Boolean.FALSE);
            primaryStage.centerOnScreen();
            primaryStage.show();
            primaryStage.setOnCloseRequest(event -> {backUp.doBackUp();System.exit(0);});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Este método se encarga de mostrar la ventana para realizar una transacción.
     * @param client (Cliente que realiza la acción)
     * @param account (Cuenta del cliente)
     */
    public void showPerformTransactionWindow(Client client, Account account) {
        try {
            logger.info("El cliente " + client.getNIF() + " ha solicitado la ventana de realizar transacción.");
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/Client/ClientPerformTransaction.fxml")));
            Parent root = loader.load();
            performTransaction = loader.getController();
            performTransaction.setData(client, account);
            primaryStage.setTitle("HappyBank - Realizar Transacción");
            primaryStage.setScene(new Scene(root));
            primaryStage.getIcons().add(happyBankLogo);
            primaryStage.resizableProperty().setValue(Boolean.FALSE);
            primaryStage.centerOnScreen();
            primaryStage.show();
            primaryStage.setOnCloseRequest(event -> {backUp.doBackUp();System.exit(0);});
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Este método se encarga de mostrar los datos personales de un cliente.
     * @param client (Cliente que realiza la acción)
     */
    public void showPersonalData(Client client) {
        try {
            logger.info("El cliente " + client.getNIF() + " ha solicitado la ventana de datos personales.");
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/Client/ClientPersonalData.fxml")));
            Parent root = loader.load();
            personalData = loader.getController();
            personalData.setData(client);
            primaryStage.setTitle("HappyBank - Datos Personales");
            primaryStage.setScene(new Scene(root));
            primaryStage.getIcons().add(happyBankLogo);
            primaryStage.resizableProperty().setValue(Boolean.FALSE);
            primaryStage.centerOnScreen();
            primaryStage.show();
            primaryStage.setOnCloseRequest(event -> {backUp.doBackUp();System.exit(0);});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Este método se encarga de mostrar el historial de transacciones de un cliente.
     * @param client (Cliente que realiza la acción)
     */
    public void showTransactionList(Client client) {
        try {
            logger.info("El cliente " + client.getNIF() + " ha solicitado el historial de transacciones.");
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/Client/ClientTransactionList.fxml")));
            Parent root = loader.load();
            transactionList = loader.getController();
            transactionList.setData(client);
            primaryStage.setTitle("HappyBank - Historial de Transacciones");
            primaryStage.setScene(new Scene(root));
            primaryStage.getIcons().add(happyBankLogo);
            primaryStage.resizableProperty().setValue(Boolean.FALSE);
            primaryStage.centerOnScreen();
            primaryStage.show();
            primaryStage.setOnCloseRequest(event -> {backUp.doBackUp();System.exit(0);});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Este método se encarga de mostrar la ventana de la leyenda de la parte de cliente.
     */
    public void showClientLegend() {
        try {
            // Cargar el archivo FXML del popup
            logger.info("Se ha solicitado la leyenda de la parte de cliente.");
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/Client/ClientLegend.fxml")));
            Parent popupRoot = loader.load();
            legendStage = new Stage();
            legendStage.resizableProperty().setValue(Boolean.FALSE);
            legendStage.setTitle("Leyenda");
            legendStage.getIcons().add(happyBankLogo);
            legendStage.setScene(new Scene(popupRoot));
            legendStage.centerOnScreen();
            legendStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
/* Auxiliar Methods */
    /**
     * Este método se encarga de cerrar las ventanas extra de la aplicación.
     */
    public void closePopup() {
        popupStage.close();
    }

    /**
     * Este método se encarga de cerrar la ventana de la leyenda.
     */
    public void closeLegend() {
        legendStage.close();
    }

    /**
     * Este método se encarga de cerrar la ventana del concepto de la transacción.
     */
    public void closeConcept() {
        conceptStage.close();
    }

    /**
     * Este método se encarga de mostrar el concepto de una transacción.
     * @param concept (Concepto de la transacción)
     * @param index (ID de la transacción)
     * @param coordinates (Coordenadas de la ventana)
     */
    public void showConcept(String concept, int index, Point2D coordinates) {
        try {
            logger.info("Se ha solicitado el concepto de la transacción " + index + ".");
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/TransactionConcept.fxml")));
            Parent root = loader.load();
            transactionConcept = loader.getController();
            transactionConcept.setConcept(concept);
            conceptStage.setTitle("HappyBank - Concepto de la Transacción " + index);
            conceptStage.setScene(new Scene(root));
            conceptStage.getIcons().add(happyBankLogo);
            conceptStage.resizableProperty().setValue(Boolean.FALSE);
            conceptStage.setX(coordinates.getX());
            conceptStage.setY(coordinates.getY());
            conceptStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Este método se encarga de mostrar la ventana de error de inicio de sesión.
     */
    public void showCloseSessionConfirmation() {
        try {
            logger.info("Se ha solicitado la ventana de confirmación de cierre de sesión.");
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/ConfirmCloseSession.fxml")));
            Parent root = loader.load();
            popupStage.setTitle("HappyBank - Confirmar Cierre de Sesión");
            popupStage.setScene(new Scene(root));
            popupStage.getIcons().add(happyBankLogo);
            popupStage.resizableProperty().setValue(Boolean.FALSE);
            popupStage.centerOnScreen();
            popupStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Este método se encarga de mostrar la ventana de error.
     * @param fxmlLoader (cargador del fxml del error correspondiente)
     */
    public void showError(FXMLLoader fxmlLoader) {
        try {
            Parent popupRoot = fxmlLoader.load();
            popupStage = new Stage();
            popupStage.resizableProperty().setValue(Boolean.FALSE);
            popupStage.setTitle("HappyBank - ERROR");
            popupStage.setScene(new Scene(popupRoot));
            popupStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/warnIcon.png")));
            popupStage.centerOnScreen();
            popupStage.show();
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3),
                    event -> this.closePopup()
            ));
            timeline.setCycleCount(1);
            timeline.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
