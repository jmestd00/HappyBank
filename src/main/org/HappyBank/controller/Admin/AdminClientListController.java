package org.HappyBank.controller.Admin;

import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.HappyBank.model.*;
import org.HappyBank.view.ViewFactory;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Controlador de la ventana de lista de clientes del administrador.
 */
public class AdminClientListController {
private static final Logger logger = LogManager.getLogger(AdminClientListController.class.getName());
    private ViewFactory viewFactory = ViewFactory.getInstance(null);
    private BankService bankService = new BankService();
    private Administrator administrator;
    @FXML
    private Label welcomeLabel;
    private String username;
    private boolean backUp = false;

    @FXML
    private TableView<Client> clientTable;
    @FXML
    private TableColumn<Client, String> NIFCol;
    @FXML
    private TableColumn<Client, String> NameCol;
    @FXML
    private TableColumn<Client, String> accountCol;
    @FXML
    private TableColumn<Client, String> creditCardCol;
    @FXML
    private TableColumn<Client, Void> editCol;
    @FXML
    private TableColumn<Client, Void> transactionHistoryCol;
    @FXML
    private TextField searchBar;
    @FXML
    private Rectangle slider;
    @FXML
    private Rectangle background;
    @FXML
    private Text off;
    @FXML
    private Text on;
    @FXML
    private Pagination pagination;
    private final int ROWS_PER_PAGE = 7;

    private ArrayList<Client> fullListClients = new ArrayList<>();
    private ArrayList<Client> filteredList = new ArrayList<>();

    /**
     * Inicializa la ventana de lista de clientes del administrador.
     */
    public void initialize(){
        readConfig();
        searchBar.setContextMenu(new ContextMenu());
        setupData();
        clientTable.setSelectionModel(null);
        NIFCol.setReorderable(false);
        NameCol.setReorderable(false);
        accountCol.setReorderable(false);
        creditCardCol.setReorderable(false);
        editCol.setReorderable(false);
        transactionHistoryCol.setReorderable(false);
        NIFCol.setCellValueFactory(data -> {
        if (data.getValue() == null) {
        return new SimpleStringProperty("");
        } else {
            return new SimpleStringProperty(data.getValue().getNIF());
        }
        });
        NameCol.setCellValueFactory(data -> {
        if (data.getValue() == null) {
        return new SimpleStringProperty("");
        } else {
            return new SimpleStringProperty(data.getValue().getName() + " " + data.getValue().getSurname());
        }
        });
        accountCol.setCellValueFactory(data -> {
        if (data.getValue() == null) {
        return new SimpleStringProperty("");
        } else {
            return new SimpleStringProperty(bankService.getAccount(data.getValue()).getIBAN());
        }
        });
        creditCardCol.setCellValueFactory(data -> {
        if (data.getValue() == null) {
        return new SimpleStringProperty("");
        } else {
            return new SimpleStringProperty(bankService.getCreditCard(bankService.getAccount(data.getValue())).getNumber());
        }
        });
        editCol.setCellFactory(param -> new TableCell<Client, Void>() {
            private final Button editButton = new Button("");
            {
                editButton.setOnAction(event -> {
                    Client selectedClient = getTableView().getItems().get(getIndex());
                    if (selectedClient != null) {
                        viewFactory.showAdminClientEditView(selectedClient, administrator);
                    }
                });

                editButton.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/Admin/AdminClientList.css")).toString());
                editButton.getStyleClass().add("editButton");
                editButton.setPrefHeight(53);
                editButton.setPrefWidth(53);
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    Client selectedClient = getTableView().getItems().get(getIndex());
                    if (selectedClient != null) {
                        setGraphic(editButton);  // Mostrar el botón si el Cliente es válido
                        setAlignment(Pos.CENTER);  // Alinear el botón
                    } else {
                        setGraphic(null);  // No mostrar el botón si el Cliente es nulo
                    }
                }
            }
        });
        transactionHistoryCol.setCellFactory(param -> new TableCell<Client, Void>() {
            private final Button transactionHistoryButton = new Button("");
            {
                transactionHistoryButton.setOnAction(event -> {
                    Client selectedClient = getTableView().getItems().get(getIndex());
                    if (selectedClient != null) {
                        viewFactory.showAdminTransactionHistoryView(selectedClient, administrator);
                    }
                });

                transactionHistoryButton.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/Admin/AdminClientList.css")).toString());
                transactionHistoryButton.getStyleClass().add("transactionHistoryButton");
                transactionHistoryButton.setPrefHeight(53);
                transactionHistoryButton.setPrefWidth(53);
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    Client selectedClient = getTableView().getItems().get(getIndex());
                    if (selectedClient != null) {
                        setGraphic(transactionHistoryButton);
                        setAlignment(Pos.CENTER);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });
        int totalPage = (int) (Math.ceil(fullListClients.size() * 1.0 / ROWS_PER_PAGE));
        pagination.setPageCount(totalPage);
        pagination.setCurrentPageIndex(0);
        changeTableView(0, ROWS_PER_PAGE);
        pagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            changeTableView(newValue.intValue(), ROWS_PER_PAGE);
        });
    }

    /**
     * Método que cambia la vista de la tabla de clientes.
     * @param index (índice de la página)
     * @param limit (límite de la página)
     */
    private void changeTableView(int index, int limit) {
        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, fullListClients.size());
        int minIndex = Math.min(toIndex, fullListClients.size());

        ObservableList<Client> pageData = FXCollections.observableArrayList(fullListClients.subList(fromIndex, minIndex));

        int remainingRows = limit - pageData.size();
        for (int i = 0; i < remainingRows; i++) {
            pageData.add(null);
        }

        SortedList<Client> sortedData = new SortedList<>(pageData);
        sortedData.comparatorProperty().bind(clientTable.comparatorProperty());

        clientTable.setItems(sortedData);
    }

    /**
     * Método que refresca la tabla de clientes.
     */
    public void refreshTable() {
        int totalPage = (int) Math.ceil(fullListClients.size() * 1.0 / ROWS_PER_PAGE);
        pagination.setPageCount(totalPage);

        changeTableView(pagination.getCurrentPageIndex(), ROWS_PER_PAGE);
    }

    /**
     * Método que establece los datos de los clientes.
     */
    private void setupData() {
        fullListClients = bankService.getAllClients();
    }

    /**
     * Método que muestra la ventana de confirmación del cierre de sesión del administrador.
     */
    public void closeSession() {
        viewFactory.showCloseSessionConfirmation();
    }

    /**
     * Método que muestra la ventana principal del administrador.
     */
    public void goBackToMain() {
        viewFactory.showAdminMainWindow(administrator.getNIF());
    }

    /**
     * Método que muestra la leyenda de la aplicación.
     */
    public void showLegend() {
        viewFactory.closeLegend();
        viewFactory.showAdminLegend();
    }

    /**
     * Método que muestra la ventana de añadir un cliente.
     */
    public void showAdd() {
        viewFactory.showAddClient(administrator);
    }

    /**
     * Método que filtra en la tabla por nombre, apellido o NIF.
     */
    public void search() {
        String filter = searchBar.getText().toLowerCase();
        if (!searchBar.getText().equals("")) {
            logger.info("El administrador " + administrator.getNIF() + " ha buscado a un cliente usando el siguiente filtro: " +filter + ".");
        fullListClients.clear();
        fullListClients = bankService.searchClients(filter, filter, filter);
        refreshTable();
        } else {
            logger.info("El administrador " + administrator.getNIF() + " ha buscado a un cliente sin filtros.");
        fullListClients.clear();
        fullListClients = bankService.getAllClients();
        refreshTable();
        }
    }

    /**
     * Método que lee la configuración del archivo options.config para ver si la opción de backup está activada.
     */
    public void readConfig() {
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
        if (backUp) {
            slider.setTranslateX(63);
            background.setStyle("-fx-fill: lightgreen;");
            off.setVisible(false);
            on.setVisible(true);
        } else {
            slider.setTranslateX(0);
            background.setStyle("-fx-fill: red;");
            off.setVisible(true);
            on.setVisible(false);
        }
    }

    /**
     * Método que escribe la configuración en el archivo options.config.
     * @param backUp (opción de backup)
     */
    private void writeConfig(boolean backUp) {
        String resourceName = "options.config";
        File externalFile = new File("options.config");

        // Copiar el archivo desde el classpath al sistema de archivos si no existe
        if (!externalFile.exists()) {
            try (InputStream input = AdminClientListController.class.getClassLoader().getResourceAsStream(resourceName)) {
                if (input == null) {
                    return;
                }

                Files.copy(input, externalFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Escribir en el archivo copiado
        try (FileWriter writer = new FileWriter(externalFile, false)) { // 'true' para agregar contenido
            writer.write("bbddBackup=" + backUp);
            logger.info("Configuración de backup de la base de datos actualizada.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método que activa o desactiva la opción de backup de la base de datos.
     */
    public void bbddBackUp() {
        backUp = !backUp;
        writeConfig(backUp);
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.25), slider);
        if (backUp) {
            logger.info("El backup de la base de datos está activado.");
            transition.setToX(63); // Mover a la derecha
            background.setStyle("-fx-fill: lightgreen;");
            off.setVisible(false);
            on.setVisible(true);
        } else {
            logger.info("El backup de la base de datos está desactivado.");
            transition.setToX(0); // Mover a la izquierda
            background.setStyle("-fx-fill: red;");
            off.setVisible(true);
            on.setVisible(false);
        }
        transition.play();
    }

    /**
     * Método que establece el administrador.
     * @param admin (administrador a establecer)
     */
    public void setAdmin(Administrator admin) {
        this.administrator = admin;
        username = admin.getName() + " " + admin.getSurname();
        welcomeLabel.setText("¡Hola " + username + "!\nBienvenido al panel de \nadministración de HappyBank.");
    }

}
